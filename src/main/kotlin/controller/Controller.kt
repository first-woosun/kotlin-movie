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
import domain.timetable.MockTimeTable
import domain.timetable.TimeTable
import domain.timetable.items.Screen
import domain.timetable.items.ScreeningSchedule
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
            val reservation = makeReserve(reservations)
            reservations.addReservation(reservation)
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

    fun makeReserve(reservations: Reservations): Reservation {
        val titleSearchResult = searchMovieWithTitle()
        val dateSearchResult = searchMovieWithDate(titleSearchResult)
        val selectedSchedule = selectMovieSchedule(dateSearchResult, reservations)
        val selectedSeats = selectSeats(selectedSchedule)
        val reservation =
            Reservation(
                movie = selectedSchedule.getMovie(),
                screenTime = selectedSchedule.getScreenTime(),
                seats = selectedSeats,
            )

        return reservation
    }

    fun searchMovieWithTitle(): TimeTable {
        try {
            val title = Title(inputView.readMovieTitle())
            return timeTable.getMovieSchedulesWithTitle(title)
        } catch (e: IllegalArgumentException) {
            outputView.printError(e.message!!)
            return searchMovieWithTitle()
        }
    }

    fun searchMovieWithDate(timeTable: TimeTable): TimeTable {
        try {
            val date = inputView.readDate()
            val localDate = LocalDate.of(date[0], date[1], date[2])
            return timeTable.getMovieSchedulesWithDate(localDate)
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
            seatNumbers.forEach {
                if (screeningSchedule.isReservedSeat(it)) {
                    outputView.printError("해당 좌석은 이미 예매되어 있습니다.")
                    return selectSeats(screeningSchedule)
                }
            }
            val seats = mutableListOf<Seat>()
            seatNumbers.forEach {
                seats.add(Seat(it))
            }
            return seats.toList()
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

        outputView.printFinalPrice(finalPrice.getAmount())

        if (!inputView.readPayAgreement()) return

        outputView.printReceipt(
            reservations.reservations,
            finalPrice.getAmount(),
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

    fun usePoint(
        price: Money
    ): Money {
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

    fun applyPayMethodDiscount(
        price: Money
    ): Money {
        val payMethodDiscountPolicy = getUsePayMethod()
        return price.applyPayMethod(payMethodDiscountPolicy)
    }
}
