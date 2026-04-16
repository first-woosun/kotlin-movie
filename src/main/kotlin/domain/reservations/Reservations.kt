package domain.reservations

import domain.discountpolicy.MovieDayDiscountPolicy
import domain.discountpolicy.TimeDiscountPolicy
import domain.money.Money
import domain.movie.Movie
import domain.reservations.items.Reservation
import domain.seat.Seat
import domain.timetable.items.ScreenTime

class Reservations {
    private val _reservations = mutableListOf<Reservation>()

    val reservations get() = _reservations.toList()

    fun addReservation(
        movie: Movie,
        screenTime: ScreenTime,
        seats: List<Seat>
    ) {
        _reservations.add(Reservation(
            movie = movie,
            screenTime = screenTime,
            seats = seats
        ))
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
