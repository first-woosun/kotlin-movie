package domain.reservations.items

import domain.money.Money
import domain.movie.itmes.Title
import domain.seat.Seat
import domain.timetable.items.ScreenTime
import java.time.LocalDate
import java.time.LocalTime

data class ReservationInfo(
    val title: String,
    val startTime: LocalTime,
    val screeningDate: LocalDate,
    val seats: List<Seat>
)
