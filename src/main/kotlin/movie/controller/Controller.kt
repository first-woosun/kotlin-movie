package movie.controller

import movie.domain.discountpolicy.CardDiscountPolicy
import movie.domain.discountpolicy.CashDiscountPolicy
import movie.domain.discountpolicy.MovieDayDiscountPolicy
import movie.domain.discountpolicy.PayMethod
import movie.domain.discountpolicy.PayMethodDiscountPolicy
import movie.domain.discountpolicy.TimeDiscountPolicy
import movie.domain.money.Money
import movie.domain.movie.itmes.Title
import movie.domain.point.Point
import movie.domain.reservations.Reservations
import movie.domain.reservations.items.Reservation
import movie.domain.seat.Seat
import movie.domain.timetable.TimeTable
import movie.domain.timetable.items.Screen
import movie.domain.timetable.items.ScreeningSchedule
import movie.parser.DateParser
import movie.parser.SeatParser
import movie.repository.MovieRepository
import movie.repository.ReservationRepository
import movie.repository.ScheduleRepository
import movie.view.input.InputView
import movie.view.output.OutputView

class Controller(
    private val movieRepository: MovieRepository,
    private val scheduleRepository: ScheduleRepository,
    private val reservationRepository: ReservationRepository,
    private val inputView: InputView = InputView,
    private val outputView: OutputView = OutputView,
    private val timeDiscountPolicy: TimeDiscountPolicy = TimeDiscountPolicy(Money(2000)),
    private val movieDayDiscountPolicy: MovieDayDiscountPolicy = MovieDayDiscountPolicy(0.9),
    private val cardDiscountPolicy: CardDiscountPolicy = CardDiscountPolicy(0.95),
    private val cashDiscountPolicy: CashDiscountPolicy = CashDiscountPolicy(0.9),
) {
    fun run() {
        val reservations = Reservations()

        if (!startReserve()) return

        do {
            makeReserve(reservations)
        } while (continueReserve())

        payProcessor(reservations)
    }

    private fun startReserve(): Boolean {
        try {
            return inputView.readStartReserve()
        } catch (e: IllegalArgumentException) {
            outputView.printError(e.message!!)
            return startReserve()
        }
    }

    private fun makeReserve(reservations: Reservations) {
        val titleSearchResult = searchMovieWithTitle()
        val dateSearchResult = searchMovieWithDate(titleSearchResult)
        val selectedSchedule = selectMovieSchedule(dateSearchResult, reservations)
        val selectedSeats = selectSeats(selectedSchedule)

        reservations.addReservation(
            scheduleId = selectedSchedule.id!!,
            movie = selectedSchedule.getMovie(),
            screenTime = selectedSchedule.getScreenTime(),
            seats = selectedSeats,
        )

        outputView.printAddReservation(
            Reservation(
                scheduleId = selectedSchedule.id,
                movie = selectedSchedule.getMovie(),
                screenTime = selectedSchedule.getScreenTime(),
                seats = selectedSeats,
            ),
        )
    }

    private fun searchMovieWithTitle(): TimeTable {
        try {
            val title = Title(inputView.readMovieTitle())
            val result = scheduleRepository.findAllByTitle(title)
            if (result.isEmpty()) {
                outputView.printError("해당 영화는 상영하고 있지 않습니다.")
                return searchMovieWithTitle()
            }
            return result
        } catch (e: IllegalArgumentException) {
            outputView.printError(e.message!!)
            return searchMovieWithTitle()
        }
    }

    private fun searchMovieWithDate(timeTable: TimeTable): TimeTable {
        try {
            val value = inputView.readDate()
            val date = DateParser.parse(value)
            val result = timeTable.getMovieSchedulesWithDate(date)
            if (result.isEmpty()) {
                outputView.printError("해당 일자의 상영 계획이 없습니다.")
                return searchMovieWithDate(timeTable)
            }
            return result
        } catch (e: IllegalArgumentException) {
            outputView.printError(e.message!!)
            return searchMovieWithDate(timeTable)
        }
    }

    private fun selectMovieSchedule(
        timeTable: TimeTable,
        reservations: Reservations,
    ): ScreeningSchedule {
        outputView.printScreeningMovieTime(timeTable.getSchedules())
        try {
            val index = inputView.readScreeningNumber(timeTable.countSchedule())
            val selectedSchedule = timeTable.getScheduleWithIndex(index - 1)

            if (reservations.checkDuplicate(selectedSchedule.getScreenTime())) {
                outputView.printError("선택하신 상영 시간이 겹칩니다. 다른 시간을 선택해 주세요.")
                return selectMovieSchedule(timeTable, reservations)
            }
            return selectedSchedule
        } catch (e: IllegalArgumentException) {
            outputView.printError(e.message!!)
            return selectMovieSchedule(timeTable, reservations)
        }
    }

    private fun selectSeats(screeningSchedule: ScreeningSchedule): List<Seat> {
        val reservedSeatsInDb = reservationRepository.findReservedSeatsByScheduleId(screeningSchedule.id!!)

        outputView.printSeatMap(Screen.Companion.seatMap)
        try {
            val seatNumbers = inputView.readSeatNumber()
            val seats = SeatParser.parse(seatNumbers)

            if (screeningSchedule.isReservedSeat(seats) || seats.any { it in reservedSeatsInDb }) {
                outputView.printError("이미 예매된 좌석입니다.")
                return selectSeats(screeningSchedule)
            }
            return seats
        } catch (e: IllegalArgumentException) {
            outputView.printError(e.message!!)
            return selectSeats(screeningSchedule)
        }
    }

    private fun continueReserve(): Boolean {
        try {
            return inputView.readContinue()
        } catch (e: IllegalArgumentException) {
            outputView.printError(e.message!!)
            return continueReserve()
        }
    }

    private fun payProcessor(reservations: Reservations) {
        val reservationItems = reservations.reservations
        outputView.printFinalReservations(reservationItems)

        val totalPrice = reservations.getDiscountedTotalPrice(timeDiscountPolicy, movieDayDiscountPolicy)
        val pointAppliedPrice = usePoint(totalPrice)
        val finalPrice = applyPayMethodDiscount(pointAppliedPrice)

        outputView.printFinalPrice(finalPrice.amount)

        if (!inputView.readPayAgreement()) return

        saveAllToDatabase(reservations)

        outputView.printReceipt(
            reservations.reservations,
            finalPrice.amount,
        )
    }

    private fun saveAllToDatabase(reservations: Reservations) {
        reservations.reservations.forEach { reservation ->
            val totalPrice = reservation.price(timeDiscountPolicy, movieDayDiscountPolicy)
            reservationRepository.save(reservation.scheduleId!!, reservation, totalPrice)
        }
    }

    private fun getUsePoint(): Point {
        try {
            return Point(inputView.readUsePoint())
        } catch (e: IllegalArgumentException) {
            outputView.printError(e.message!!)
            return getUsePoint()
        }
    }

    private fun usePoint(price: Money): Money {
        try {
            val point = getUsePoint()
            if (point.isBiggerThan(price.amount)) {
                outputView.printError("사용할 포인트는 금액보다 클 수 없습니다.")
                return usePoint(price)
            }
            return price.applyPoint(point.amount)
        } catch (e: IllegalArgumentException) {
            outputView.printError(e.message!!)
            return usePoint(price)
        }
    }

    private fun getUsePayMethod(): PayMethodDiscountPolicy {
        try {
            val payMethod = inputView.readPayMethod()
            return PayMethod.Companion.toPolicy(
                payMethod = payMethod,
                cardDiscountPolicy = cardDiscountPolicy,
                cashDiscountPolicy = cashDiscountPolicy,
            )
        } catch (e: IllegalArgumentException) {
            outputView.printError(e.message!!)
            return getUsePayMethod()
        }
    }

    private fun applyPayMethodDiscount(price: Money): Money {
        val payMethodDiscountPolicy = getUsePayMethod()
        return price.applyPayMethod(payMethodDiscountPolicy)
    }
}
