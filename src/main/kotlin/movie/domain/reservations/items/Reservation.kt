package movie.domain.reservations.items

import movie.domain.discountpolicy.MovieDayDiscountPolicy
import movie.domain.discountpolicy.TimeDiscountPolicy
import movie.domain.money.Money
import movie.domain.movie.Movie
import movie.domain.seat.Seat
import movie.domain.timetable.items.ScreenTime
import java.time.LocalDate
import java.time.LocalTime

class Reservation(
    val id: Int? = null,
    val scheduleId: Int? = null,
    private val movie: Movie,
    private val screenTime: ScreenTime,
    private val seats: List<Seat>,
) {
    fun isDuplicatedDate(date: LocalDate): Boolean = screenTime.screeningDate == date

    fun isDuplicatedTime(time: LocalTime): Boolean = screenTime.isContain(time)

    fun price(
        timeDiscountPolicy: TimeDiscountPolicy,
        movieDayDiscountPolicy: MovieDayDiscountPolicy,
    ): Money {
        var totalPrice = Money(0)
        seats.forEach {
            var price = it.getPrice()
            price = movieDayDiscountPolicy.applyDiscount(price, screenTime)
            price = timeDiscountPolicy.applyDiscount(price, screenTime)
            totalPrice = price
        }
        return totalPrice
    }

    fun getReservationInfo(): ReservationInfo =
        ReservationInfo(
            title = movie.getTitleText(),
            startTime = screenTime.startTime,
            screeningDate = screenTime.screeningDate,
            seats = seats,
        )
}
