package domain.timetable.items

import domain.movie.Movie
import domain.movie.itmes.Title
import domain.seat.Seat
import java.time.LocalDate

class ScreeningSchedule(
    private val movie: Movie,
    private val screenTime: ScreenTime,
) {
    private val reservedSeat: MutableList<Seat> = mutableListOf()

    fun isScreeningMovieTitle(title: Title) = movie.isExist(title)

    fun isScreeningDate(date: LocalDate) = screenTime.isScreeningAt(date)

    fun isReservedSeat(seatNumber: String) = reservedSeat.any { it.isExist(seatNumber) }

    fun reserveSeat(seat: Seat) = reservedSeat.add(seat)

    fun getStartTime() = screenTime.startTimeToString()

    fun getMovie() = movie

    fun getScreenTime() = screenTime
}
