package domain.reservations.items

import domain.movie.Movie
import domain.movie.itmes.RunningTime
import domain.movie.itmes.ScreeningPeriod
import domain.movie.itmes.Title
import domain.seat.Seat
import domain.seat.items.ColumnNumber
import domain.seat.items.RowNumber
import domain.seat.items.SeatGrade
import domain.timetable.items.ScreenTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime

class ReservationTest {
    val reservation =
        Reservation(
            movie =
                Movie(
                    title = Title("신바드의 모험"),
                    runningTime = RunningTime(120),
                    screeningPeriod =
                        ScreeningPeriod(
                            startDate = LocalDate.of(2026, 4, 1),
                            endDate = LocalDate.of(2026, 4, 30),
                        ),
                ),
            screenTime =
                ScreenTime(
                    startTime = LocalTime.of(11, 0),
                    endTime = LocalTime.of(13, 0),
                    screeningDate = LocalDate.of(2026, 4, 10),
                ),
            seats =
                listOf<Seat>(
                    Seat(
                        rowNumber = RowNumber("A"),
                        columnNumber = ColumnNumber(1),
                        seatGrade = SeatGrade.GradeB
                    ),
                    Seat(
                        rowNumber = RowNumber("B"),
                        columnNumber = ColumnNumber(1),
                        seatGrade = SeatGrade.GradeS
                    ),
                ),
        )

    @Test
    fun `입력된 상영 일자가 screenTime의 screeningDate과 같다면 true를 반환받는다`() {
        val result = reservation.isDuplicatedDate(LocalDate.of(2026, 4, 10))

        assertThat(result).isTrue()
    }

    @Test
    fun `입력된 상영 일자가 screenTime의 screeningDate과 같지 않으면 false를 반환받는다`() {
        val result = reservation.isDuplicatedDate(LocalDate.of(2026, 4, 11))

        assertThat(result).isFalse()
    }

    @Test
    fun `입력된 상영 시간이 screenTime의 ScreeningTime과 중복된다면 true를 반환받는다`() {
        val result = reservation.isDuplicatedTime(LocalTime.of(11, 0))

        assertThat(result).isTrue()
    }

    @Test
    fun `입력된 상영 시간이 screenTime의 ScreeningTime과 중복되지 않으면 false를 반환받는다`() {
        val result = reservation.isDuplicatedTime(LocalTime.of(14, 0))

        assertThat(result).isFalse()
    }
}
