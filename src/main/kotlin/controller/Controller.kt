package controller

import domain.discountpolicy.CardDiscountPolicy
import domain.discountpolicy.CashDiscountPolicy
import domain.discountpolicy.MovieDayDiscountPolicy
import domain.discountpolicy.PayMethod
import domain.discountpolicy.PayMethodDiscountPolicy
import domain.discountpolicy.TimeDiscountPolicy
import domain.money.Money
import domain.movie.itmes.Title
import domain.point.Point
import domain.reservations.Reservations
import domain.reservations.items.Reservation
import domain.seat.Seat
import domain.seat.items.ColumnNumber
import domain.seat.items.RowNumber
import domain.seat.items.SeatGrade
import domain.timetable.MockTimeTable
import domain.timetable.TimeTable
import domain.timetable.items.Screen
import domain.timetable.items.ScreeningSchedule
import parser.DateParser
import parser.SeatParser
import view.input.InputView
import view.output.OutputView
import java.time.LocalDate

class Controller(
    val inputView: InputView,
    val outputView: OutputView,
    val timeDiscountPolicy: TimeDiscountPolicy,
    val movieDayDiscountPolicy: MovieDayDiscountPolicy,
    val cardDiscountPolicy: CardDiscountPolicy,
    val cashDiscountPolicy: CashDiscountPolicy,
    val timeTable: TimeTable = TimeTable(MockTimeTable.timeTable),
) {
    fun run() {
        val reservations = Reservations()

        if (!startReserve()) return

        do {
            makeReserve(reservations)
        } while (continueReserve())

        payProcessor(reservations)
    }

    fun startReserve(): Boolean {
        try {
            return inputView.readStartReserve()
        } catch (e: IllegalArgumentException) {
            outputView.printError(e.message!!)
            return startReserve()
        }
    }

    fun makeReserve(reservations: Reservations) {
        val titleSearchResult = searchMovieWithTitle()
        val dateSearchResult = searchMovieWithDate(titleSearchResult)
        val selectedSchedule = selectMovieSchedule(dateSearchResult, reservations)
        val selectedSeats = selectSeats(selectedSchedule)
        val reservation = Reservation(
            movie = selectedSchedule.getMovie(),
            screenTime = selectedSchedule.getScreenTime(),
            seats = selectedSeats,
        )
        reservations.addReservation(reservation)
        outputView.printAddReservation(reservation)
    }

    fun searchMovieWithTitle(): TimeTable {
        try {
            val title = Title(inputView.readMovieTitle())
            val result = timeTable.getMovieSchedulesWithTitle(title)
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

    fun searchMovieWithDate(timeTable: TimeTable): TimeTable {
        try {
            val value = inputView.readDate()
            val date = DateParser.parse(value)
            val result = timeTable.getMovieSchedulesWithDate(date)
            if(result.isEmpty()) {
                outputView.printError("해당 일자의 상영 계획이 없습니다.")
                return searchMovieWithDate(timeTable)
            }
            return result
        } catch (e: IllegalArgumentException) {
            outputView.printError(e.message!!)
            return searchMovieWithDate(timeTable)
        }
    }

    fun selectMovieSchedule(
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

    fun selectSeats(screeningSchedule: ScreeningSchedule): List<Seat> {
        outputView.printSeatMap(Screen.seatMap)
        try {
            val seatNumbers = inputView.readSeatNumber()
            val seats = SeatParser.parse(seatNumbers)
            if (screeningSchedule.isReservedSeat(seats)) {
                outputView.printError("이미 예매된 좌석입니다.")
                return selectSeats(screeningSchedule)
            }
            return seats
        } catch (e: IllegalArgumentException) {
            outputView.printError(e.message!!)
            return selectSeats(screeningSchedule)
        }
    }

    fun continueReserve(): Boolean {
        try {
            return inputView.readContinue()
        } catch (e: IllegalArgumentException) {
            outputView.printError(e.message!!)
            return continueReserve()
        }
    }

    fun payProcessor(reservations: Reservations) {
        val reservationItems = reservations.reservations
        outputView.printFinalReservations(reservationItems)

        val totalPrice = reservations.getDiscountedTotalPrice(timeDiscountPolicy, movieDayDiscountPolicy)
        val pointAppliedPrice = usePoint(totalPrice)
        val finalPrice = applyPayMethodDiscount(pointAppliedPrice)

        outputView.printFinalPrice(finalPrice.amount)

        if (!inputView.readPayAgreement()) return

        outputView.printReceipt(
            reservations.reservations,
            finalPrice.amount,
        )
    }

    fun getUsePoint(): Point {
        try {
            return Point(inputView.readUsePoint())
        } catch (e: IllegalArgumentException) {
            outputView.printError(e.message!!)
            return getUsePoint()
        }
    }

    fun usePoint(price: Money): Money {
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

    fun getUsePayMethod(): PayMethodDiscountPolicy {
        try {
            val payMethod = inputView.readPayMethod()
            return when (payMethod) {
                PayMethod.CARD -> cardDiscountPolicy
                PayMethod.CASH -> cashDiscountPolicy
            }
        } catch (e: IllegalArgumentException) {
            outputView.printError(e.message!!)
            return getUsePayMethod()
        }
    }

    fun applyPayMethodDiscount(price: Money): Money {
        val payMethodDiscountPolicy = getUsePayMethod()
        return price.applyPayMethod(payMethodDiscountPolicy)
    }
}
