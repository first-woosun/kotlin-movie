package movie.domain.reservations.items

import movie.domain.seat.Seat
import java.time.LocalDate
import java.time.LocalTime

data class ReservationInfo(
    val title: String,
    val startTime: LocalTime,
    val screeningDate: LocalDate,
    val seats: List<Seat>,
)
