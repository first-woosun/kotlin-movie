package movie.service

import movie.controller.dto.ReservationRequest
import movie.controller.dto.ReservationResponse
import movie.domain.discountpolicy.*
import movie.domain.money.Money
import movie.domain.point.Point
import movie.domain.reservations.items.Reservation
import movie.domain.seat.Seat
import movie.domain.seat.items.ColumnNumber
import movie.domain.seat.items.RowNumber
import movie.repository.ReservationRepository
import movie.repository.ScheduleRepository
import org.springframework.stereotype.Service

@Service
class ReservationService(
    private val scheduleRepository: ScheduleRepository,
    private val reservationRepository: ReservationRepository,
    private val timeDiscountPolicy: TimeDiscountPolicy,
    private val movieDayDiscountPolicy: MovieDayDiscountPolicy,
    private val cardDiscountPolicy: CardDiscountPolicy,
    private val cashDiscountPolicy: CashDiscountPolicy,
) {
    fun reserveAll(request: ReservationRequest): ReservationResponse {
        val domainReservations =
            request.reservations.map { req ->
                val screening =
                    scheduleRepository.findById(req.screeningId)
                        ?: throw IllegalArgumentException("ID: ${req.screeningId}인 상영 일정을 찾을 수 없습니다.")

                val seats =
                    req.seats.map {
                        val row = it.take(1)
                        val col = it.substring(1).toInt()
                        Seat.create(RowNumber(row), ColumnNumber(col))
                    }

                val reservedSeatsInDb = reservationRepository.findReservedSeatsByScheduleId(req.screeningId)
                if (seats.any { it in reservedSeatsInDb }) {
                    throw IllegalArgumentException("상영 ID ${req.screeningId}에 이미 예약된 좌석이 포함되어 있습니다.")
                }

                Reservation(
                    scheduleId = req.screeningId,
                    movie = screening.getMovie(),
                    screenTime = screening.getScreenTime(),
                    seats = seats,
                )
            }

        var totalBeforeFinalDiscount = Money(0)
        domainReservations.forEach {
            totalBeforeFinalDiscount += it.price(timeDiscountPolicy, movieDayDiscountPolicy)
        }

        val point = Point(request.usedPoints)
        if (point.isBiggerThan(totalBeforeFinalDiscount.amount)) {
            throw IllegalArgumentException("사용할 포인트(${request.usedPoints}원)가 총 결제 금액(${totalBeforeFinalDiscount.amount}원)보다 클 수 없습니다.")
        }
        val pointAppliedPrice = totalBeforeFinalDiscount.applyPoint(point.amount)

        val payMethodPolicy = getPayMethodPolicy(request.paymentMethod)
        val finalPrice = pointAppliedPrice.applyPayMethod(payMethodPolicy)

        var firstSavedId = 0
        domainReservations.forEachIndexed { index, res ->
            val individualPrice = res.price(timeDiscountPolicy, movieDayDiscountPolicy)
            reservationRepository.save(res.scheduleId!!, res, individualPrice)

            if (index == 0) firstSavedId = 1
        }

        return ReservationResponse(
            reservationId = firstSavedId,
            reservations = request.reservations,
            usedPoints = request.usedPoints,
            paymentMethod = request.paymentMethod,
            totalPrice = finalPrice.amount,
        )
    }

    private fun getPayMethodPolicy(method: String): PayMethodDiscountPolicy {
        val payMethod =
            when (method) {
                "CREDIT_CARD" -> PayMethod.CARD
                "CASH" -> PayMethod.CASH
                else -> throw IllegalArgumentException("지원하지 않는 결제 방식입니다: $method")
            }
        return PayMethod.toPolicy(payMethod, cardDiscountPolicy, cashDiscountPolicy)
    }
}
