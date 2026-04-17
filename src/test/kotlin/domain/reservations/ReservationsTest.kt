package domain.reservations

import movie.domain.movie.Movie
import movie.domain.movie.itmes.RunningTime
import movie.domain.movie.itmes.ScreeningPeriod
import movie.domain.movie.itmes.Title
import movie.domain.reservations.Reservations
import movie.domain.seat.Seat
import movie.domain.seat.items.ColumnNumber
import movie.domain.seat.items.RowNumber
import movie.domain.seat.items.SeatGrade
import movie.domain.timetable.items.ScreenTime
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
            val movie =
                Movie(
                    title = Title("신바드의 모험"),
                    runningTime = RunningTime(120),
                    screeningPeriod =
                        ScreeningPeriod(
                            startDate = LocalDate.of(2026, 4, 1),
                            endDate = LocalDate.of(2026, 4, 30),
                        ),
                )

            val screenTime =
                ScreenTime(
                    startTime = LocalTime.of(11, 0),
                    endTime = LocalTime.of(13, 0),
                    screeningDate = LocalDate.of(2026, 4, 10),
                )

            val seats =
                listOf<Seat>(
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
                scheduleId = 1,
                movie = movie,
                screenTime = screenTime,
                seats = seats,
            )
        }
    }
}
