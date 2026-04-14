package controller

import domain.discountpolicy.CardDiscountPolicy
import domain.discountpolicy.CashDiscountPolicy
import domain.discountpolicy.DiscountPolicy
import domain.discountpolicy.PayMethod
import domain.discountpolicy.PayMethodDiscountPolicy
import domain.money.Money
import domain.movie.itmes.Title
import domain.paycalculator.PayCalculator
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
    val discountPolicies: List<DiscountPolicy>,
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
            println(e.message)
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
            println(e.message)
            return searchMovieWithTitle()
        }
    }

    fun searchMovieWithDate(timeTable: TimeTable): TimeTable {
        try {
            val date = inputView.readDate()
            val localDate = LocalDate.of(date[0], date[1], date[2])
            return timeTable.getMovieSchedulesWithDate(localDate)
        } catch (e: IllegalArgumentException) {
            println(e.message)
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
                throw IllegalArgumentException("선택하신 상영 시간이 겹칩니다. 다른 시간을 선택해 주세요.")
            }
            return selectedSchedule
        } catch (e: IllegalArgumentException) {
            println(e.message)
            return selectMovieSchedule(timeTable, reservations)
        }
    }

    fun selectSeats(screeningSchedule: ScreeningSchedule): List<Seat> {
        outputView.printSeatMap(Screen.seatMap)
        try {
            val seatNumbers = inputView.readSeatNumber()
            seatNumbers.forEach {
                if (screeningSchedule.isReservedSeat(it)) {
                    throw IllegalArgumentException("해당 좌석은 이미 예매되어 있습니다.")
                }
            }
            val seats = mutableListOf<Seat>()
            seatNumbers.forEach {
                seats.add(Seat(it))
            }
            return seats.toList()
        } catch (e: IllegalArgumentException) {
            println(e.message)
            return selectSeats(screeningSchedule)
        }
    }

    fun continueReserve(): Boolean {
        try {
            return inputView.readContinue()
        } catch (e: IllegalArgumentException) {
            println(e.message)
            return continueReserve()
        }
    }

    fun payProcessor(reservations: Reservations) {
        val reservationItems = reservations.reservations
        outputView.printFinalReservations(reservationItems)
        val payCalculator = PayCalculator()

        val initPrice = payCalculator.calculateInitPrice(reservationItems)
        val discountedPrice = payCalculator.calculateTimeDiscountedPrice(initPrice, reservationItems, discountPolicies)
        val pointAppliedPrice = usePoint(discountedPrice, payCalculator)
        val finalPrice = applyPayMethodDiscount(pointAppliedPrice, payCalculator)

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
            println(e.message)
            return getUsePoint()
        }
    }

    fun usePoint(
        price: Money,
        payCalculator: PayCalculator,
    ): Money {
        try {
            val point = getUsePoint()
            return payCalculator.usePoint(price, point)
        } catch (e: IllegalArgumentException) {
            println(e.message)
            return usePoint(price, payCalculator)
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
            println(e.message)
            return getUsePayMethod()
        }
    }

    fun applyPayMethodDiscount(
        price: Money,
        payCalculator: PayCalculator,
    ): Money {
        val payMethodDiscountPolicy = getUsePayMethod()
        return payCalculator.usePayMethod(price, payMethodDiscountPolicy)
    }
}
