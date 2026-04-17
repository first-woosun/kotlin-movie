package movie.view.output

import movie.domain.reservations.items.Reservation
import movie.domain.timetable.items.ScreeningSchedule
import kotlin.collections.iterator

object OutputView {
    fun printScreeningMovieTime(schedules: List<ScreeningSchedule>) {
        println(Label.SCREENING_LIST_LABEL)
        schedules.forEachIndexed { index, schedule ->
            println("[${index + 1}] ${schedule.getStartTime()}")
        }
    }

    fun printSeatMap(seatData: Map<String, List<String>>) {
        if (seatData.isEmpty()) return
        val columns = seatData.values.first().size
        val header = (1..columns).joinToString("") { it.toString().padStart(5, ' ') }
        println(header)
        for ((rowName, seats) in seatData) {
            val seatRow = seats.joinToString(" ") { "[ $it]" }
            println("$rowName $seatRow")
        }
    }

    fun printAddReservation(reservation: Reservation) {
        println(Label.ADD_RESERVATION_LABEL)
        printReservationSummary(reservation)
    }

    fun printFinalReservations(reservations: List<Reservation>) {
        println(Label.CART_LABEL)
        reservations.forEach {
            printReservationSummary(it)
        }
    }

    fun printFinalPrice(price: Int) {
        println(Label.FINAL_PRICE_LABEL)
        println("${Label.CALCULATE_PRICE_LABEL} $price ${Label.WON}")
    }

    fun printReceipt(
        reservations: List<Reservation>,
        price: Int,
    ) {
        println(Label.SUCCESS_RESERVE_LABEL)
        println(Label.HISTORY_LABEL)
        reservations.forEach {
            printReservationSummary(it)
        }
        println("${Label.PRICE_LABEL} $price ${Label.WON}\n")
        print(Label.APPRECIATE_LABEL)
    }

    fun printReservationSummary(reservation: Reservation) {
        val reservationInfo = reservation.getReservationInfo()
        val seats = reservationInfo.seats
        println(
            "- [${reservationInfo.title}] ${reservationInfo.screeningDate} ${reservationInfo.startTime} ${seats.joinToString(
                separator = ", ",
            ) { it.getSeatNumber() }}",
        )
    }

    fun printError(error: String) {
        println(error)
    }

    object Label {
        const val SCREENING_LIST_LABEL = "해당 날짜의 상영 목록"
        const val ADD_RESERVATION_LABEL = "장바구니에 추가됨"
        const val CART_LABEL = "장바구니"
        const val CALCULATE_PRICE_LABEL = "가격 계산"
        const val FINAL_PRICE_LABEL = "최종 결제 금액: "
        const val WON = "원"
        const val SUCCESS_RESERVE_LABEL = "예매 완료"
        const val HISTORY_LABEL = "내역:"
        const val PRICE_LABEL = "결제 금액: "
        const val APPRECIATE_LABEL = "감사합니다."
    }
}
