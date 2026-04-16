package domain.reservations

import domain.movie.Movie
import domain.movie.itmes.RunningTime
import domain.movie.itmes.ScreeningPeriod
import domain.movie.itmes.Title
import domain.reservations.items.Reservation
import domain.seat.Seat
import domain.seat.items.ColumnNumber
import domain.seat.items.RowNumber
import domain.seat.items.SeatGrade
import domain.timetable.items.ScreenTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime

class ReservationsTest {
    @Test
    fun `날짜와 시간이 모두 겹치는 상영 시간이 입력되면 true를 반환한다`() {
        val screenTime =
            ScreenTime(
                startTime = LocalTime.of(11, 0),
                endTime = LocalTime.of(13, 0),
                screeningDate = LocalDate.of(2026, 4, 10),
            )
        val result = reservations.checkDuplicate(screenTime)

        assertThat(result).isTrue()
    }

    @Test
    fun `날짜는 같지만 시간이 겹치지 않으면 false를 반환한다`() {
        val screenTime =
            ScreenTime(
                startTime = LocalTime.of(14, 0),
                endTime = LocalTime.of(16, 0),
                screeningDate = LocalDate.of(2026, 4, 10),
            )
        val result = reservations.checkDuplicate(screenTime)

        assertThat(result).isFalse()
    }

    @Test
    fun `시간은 같지만 날짜가 다르면 false를 반환한다`() {
        val screenTime =
            ScreenTime(
                startTime = LocalTime.of(11, 0),
                endTime = LocalTime.of(13, 0),
                screeningDate = LocalDate.of(2026, 4, 11),
            )
        val result = reservations.checkDuplicate(screenTime)

        assertThat(result).isFalse()
    }

    companion object {
        private val reservations = Reservations()

        @BeforeAll
        @JvmStatic
        fun setUpReservation() {
            val movie = Movie(
                title = Title("신바드의 모험"),
                runningTime = RunningTime(120),
                screeningPeriod =
                    ScreeningPeriod(
                        startDate = LocalDate.of(2026, 4, 1),
                        endDate = LocalDate.of(2026, 4, 30),
                    ),
            )

            val screenTime = ScreenTime(
                startTime = LocalTime.of(11, 0),
                endTime = LocalTime.of(13, 0),
                screeningDate = LocalDate.of(2026, 4, 10),
            )

            val seats = listOf<Seat>(
                Seat(
                    rowNumber = RowNumber("A"),
                    columnNumber = ColumnNumber(1),
                    seatGrade = SeatGrade.GradeB,
                ),
                Seat(
                    rowNumber = RowNumber("B"),
                    columnNumber = ColumnNumber(1),
                    seatGrade = SeatGrade.GradeS,
                ),
            )

            reservations.addReservation(
                movie = movie,
                screenTime = screenTime,
                seats = seats
            )
        }
    }
}
