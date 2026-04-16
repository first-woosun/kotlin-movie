package domain.timetable

import domain.movie.Movie
import domain.movie.itmes.RunningTime
import domain.movie.itmes.ScreeningPeriod
import domain.movie.itmes.Title
import domain.timetable.items.ScreenTime
import domain.timetable.items.ScreeningSchedule
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import java.time.LocalTime

class TimeTableTest {
    val timeTable =
        TimeTable(
            listOf(
                createSchedule(
                    movieTitle = Title("신바드의 모험"),
                    screenTime =
                        ScreenTime(
                            startTime = LocalTime.of(11, 0),
                            endTime = LocalTime.of(12, 0),
                            screeningDate = LocalDate.of(2026, 4, 8),
                        ),
                ),
                createSchedule(
                    movieTitle = Title("신밧드의 모험"),
                    screenTime =
                        ScreenTime(
                            startTime = LocalTime.of(12, 0),
                            endTime = LocalTime.of(13, 0),
                            screeningDate = LocalDate.of(2026, 4, 9),
                        ),
                ),
                createSchedule(
                    movieTitle = Title("신바드의 모험"),
                    screenTime =
                        ScreenTime(
                            startTime = LocalTime.of(14, 0),
                            endTime = LocalTime.of(15, 0),
                            screeningDate = LocalDate.of(2026, 4, 9),
                        ),
                ),
            ),
        )

    @Test
    fun `영화 제목을 입력받아 입력값과 같은 영화 제목을 갖는 screening schedule의 목록을 TimeTable 형태로 반환한다`() {
        val result = timeTable.getMovieSchedulesWithTitle(Title("신바드의 모험"))

        assertThat(result.countSchedule()).isEqualTo(2)
    }

    @Test
    fun `입력받은 영화 제목이 screening schedule의 목록 중 일치하는 스케쥴이 없다면 예외를 발생시킨다`() {
        assertThrows<IllegalArgumentException> { timeTable.getMovieSchedulesWithTitle(Title("심바드의 모험")) }
    }

    @Test
    fun `상영 일자를 입력받아 입력값과 같은 상영 일자를 갖는 screening schedule의 목록을 TimeTable 형태로 반환한다`() {
        val result = timeTable.getMovieSchedulesWithDate(LocalDate.of(2026, 4, 9))
        assertThat(result.countSchedule()).isEqualTo(2)
    }

    @Test
    fun `입력받은 상영 일자가 screening schedule의 목록 중 일치하는 스케쥴이 없다면 예외를 발생시킨다`() {
        assertThrows<IllegalArgumentException> { timeTable.getMovieSchedulesWithDate(LocalDate.of(2020, 2, 2)) }
    }

    private fun createSchedule(
        movieTitle: Title,
        screenTime: ScreenTime,
    ): ScreeningSchedule =
        ScreeningSchedule(
            Movie(
                title = movieTitle,
                runningTime = RunningTime(120),
                screeningPeriod =
                    ScreeningPeriod(
                        startDate =
                            LocalDate.of(
                                2026,
                                4,
                                1,
                            ),
                        endDate =
                            LocalDate.of(
                                2026,
                                4,
                                30,
                            ),
                    ),
            ),
            screenTime = screenTime,
        )
}
