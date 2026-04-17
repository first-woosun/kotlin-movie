package domain.timetable.items

import movie.domain.movie.Movie
import movie.domain.movie.itmes.RunningTime
import movie.domain.movie.itmes.ScreeningPeriod
import movie.domain.movie.itmes.Title
import movie.domain.seat.Seat
import movie.domain.seat.items.ColumnNumber
import movie.domain.seat.items.RowNumber
import movie.domain.timetable.items.ScreenTime
import movie.domain.timetable.items.ScreeningSchedule
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime

class ScreeningScheduleTest {
    private val schedule = createSchedule()

    @Test
    fun `입력된 영화 제목이 소유하고 있는 영화 제목과 같다면 true를 반환받는다`() {
        val result = schedule.isScreeningMovieTitle(Title("신바드의 모험"))

        assertThat(result).isTrue()
    }

    @Test
    fun `입력된 영화 제목이 소유하고 있는 영화 제목과 다르다면 false를 반환받는다`() {
        val result = schedule.isScreeningMovieTitle(Title("신밧드의 모험"))

        assertThat(result).isFalse()
    }

    @Test
    fun `입력된 상영 일자가 소유하고 있는 영화 상영 일자와 같다면 true를 반환받는다`() {
        val result = schedule.isScreeningDate(LocalDate.of(2026, 4, 10))

        assertThat(result).isTrue()
    }

    @Test
    fun `입력된 상영 일자가 소유하고 있는 영화 상영 일자와 다르다면 false를 반환받는다`() {
        val result = schedule.isScreeningDate(LocalDate.of(2026, 4, 1))

        assertThat(result).isFalse()
    }

    @Test
    fun `입력된 좌석 번호가 reservedSeats에 있다면 true를 반환한다`() {
        val reserveSeat = Seat.create(RowNumber("A"), ColumnNumber(1))
        schedule.reserveSeat(reserveSeat)

        val result = schedule.isReservedSeat(listOf(Seat.create(RowNumber("A"), ColumnNumber(1))))

        assertThat(result).isTrue()
    }

    @Test
    fun `입력된 좌석 번호가 reservedSeats에 없다면 false를 반환한다`() {
        val reserveSeat = Seat.create(RowNumber("A"), ColumnNumber(1))
        schedule.reserveSeat(reserveSeat)

        val result = schedule.isReservedSeat(listOf(Seat.create(RowNumber("B"), ColumnNumber(1))))

        assertThat(result).isFalse()
    }

    private fun createSchedule() =
        ScreeningSchedule(
            id = 1,
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
        )
}
