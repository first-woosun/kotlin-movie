package movie.domain.timetable.items

import movie.domain.movie.Movie
import movie.domain.movie.itmes.Title
import movie.domain.seat.Seat
import java.time.LocalDate

class ScreeningSchedule(
    val id: Int? = null,
    private val movie: Movie,
    private val screenTime: ScreenTime,
) {
    private val reservedSeat: MutableList<Seat> = mutableListOf()

    fun isScreeningMovieTitle(title: Title) = movie.isSameTitle(title)

    fun isScreeningDate(date: LocalDate) = screenTime.isScreeningAt(date)

    fun isReservedSeat(seats: List<Seat>): Boolean = seats.any { it in reservedSeat }

    fun reserveSeat(seat: Seat) = reservedSeat.add(seat)

    fun getStartTime() = screenTime.startTimeToString()

    fun getMovie() = movie

    fun getScreenTime() = screenTime
}
