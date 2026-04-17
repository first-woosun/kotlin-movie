package movie.domain.reservations

import movie.domain.discountpolicy.MovieDayDiscountPolicy
import movie.domain.discountpolicy.TimeDiscountPolicy
import movie.domain.money.Money
import movie.domain.movie.Movie
import movie.domain.reservations.items.Reservation
import movie.domain.seat.Seat
import movie.domain.timetable.items.ScreenTime

class Reservations {
    private val _reservations = mutableListOf<Reservation>()

    val reservations get() = _reservations.toList()

    fun addReservation(
        scheduleId: Int,
        movie: Movie,
        screenTime: ScreenTime,
        seats: List<Seat>,
    ) {
        _reservations.add(
            Reservation(
                scheduleId = scheduleId,
                movie = movie,
                screenTime = screenTime,
                seats = seats,
            ),
        )
    }

    fun checkDuplicate(screenTime: ScreenTime): Boolean {
        val startTime = screenTime.startTime
        val screeningDate = screenTime.screeningDate

        return _reservations.any {
            it.isDuplicatedDate(screeningDate) && it.isDuplicatedTime(startTime)
        }
    }

    fun getDiscountedTotalPrice(
        timeDiscountPolicy: TimeDiscountPolicy,
        movieDayDiscountPolicy: MovieDayDiscountPolicy,
    ): Money {
        var price = Money(0)
        _reservations.forEach {
            price += it.price(timeDiscountPolicy, movieDayDiscountPolicy)
        }
        return price
    }
}
