package domain.timetable

import movie.domain.movie.Movie
import movie.domain.movie.itmes.RunningTime
import movie.domain.movie.itmes.ScreeningPeriod
import movie.domain.movie.itmes.Title
import movie.domain.timetable.TimeTable
import movie.domain.timetable.items.ScreenTime
import movie.domain.timetable.items.ScreeningSchedule
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime

class TimeTableTest {
    private val timeTable =
        TimeTable(
            listOf(
                createSchedule(
                    title = "신바드의 모험",
                    startTime = LocalTime.of(11, 0),
                    screeningDate = LocalDate.of(2026, 4, 8),
                ),
                createSchedule(
                    title = "신밧드의 모험",
                    startTime = LocalTime.of(12, 0),
                    screeningDate = LocalDate.of(2026, 4, 9),
                ),
                createSchedule(
                    title = "신바드의 모험",
                    startTime = LocalTime.of(14, 0),
                    screeningDate = LocalDate.of(2026, 4, 9),
                ),
            ),
        )

    @Test
    fun `영화 제목을 입력받아 입력값과 같은 영화 제목을 갖는 screening schedule의 목록을 TimeTable 형태로 반환한다`() {
        val result = timeTable.getMovieSchedulesWithTitle(Title("신바드의 모험"))

        assertThat(result.countSchedule()).isEqualTo(2)
    }

    @Test
    fun `입력받은 영화 제목이 screening schedule의 목록 중 일치하는 스케쥴이 없다면 빈 객체를 반환한다`() {
        val result = timeTable.getMovieSchedulesWithTitle(Title("심바드의 모험"))

        assertThat(result.isEmpty()).isTrue()
    }

    @Test
    fun `상영 일자를 입력받아 입력값과 같은 상영 일자를 갖는 screening schedule의 목록을 TimeTable 형태로 반환한다`() {
        val result = timeTable.getMovieSchedulesWithDate(LocalDate.of(2026, 4, 9))

        assertThat(result.countSchedule()).isEqualTo(2)
    }

    @Test
    fun `입력받은 상영 일자가 screening schedule의 목록 중 일치하는 스케쥴이 없다면 빈 객체를 반환한다`() {
        val result = timeTable.getMovieSchedulesWithDate(LocalDate.of(2026, 4, 10))

        assertThat(result.isEmpty()).isTrue()
    }

    private fun createSchedule(
        title: String,
        startTime: LocalTime,
        screeningDate: LocalDate,
    ) = ScreeningSchedule(
        movie =
            Movie(
                title = Title(title),
                runningTime = RunningTime(120),
                screeningPeriod =
                    ScreeningPeriod(
                        startDate = LocalDate.of(2026, 4, 1),
                        endDate = LocalDate.of(2026, 4, 30),
                    ),
            ),
        screenTime =
            ScreenTime(
                startTime = startTime,
                endTime = startTime.plusHours(1),
                screeningDate = screeningDate,
            ),
    )
}
