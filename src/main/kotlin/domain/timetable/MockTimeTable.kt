package domain.timetable

import domain.movie.Movie
import domain.movie.itmes.RunningTime
import domain.movie.itmes.ScreeningPeriod
import domain.movie.itmes.Title
import domain.timetable.items.ScreenTime
import domain.timetable.items.ScreeningSchedule
import java.time.LocalDate
import java.time.LocalTime

object MockTimeTable {
    private val adventureOfBard =
        Movie(
            title = Title("신바드의 모험"),
            runningTime = RunningTime(120),
            screeningPeriod =
                ScreeningPeriod(
                    startDate = LocalDate.of(2026, 4, 1),
                    endDate = LocalDate.of(2026, 4, 30),
                ),
        )

    private val ironMan =
        Movie(
            title = Title("아이언맨"),
            runningTime = RunningTime(120),
            screeningPeriod =
                ScreeningPeriod(
                    startDate = LocalDate.of(2026, 4, 1),
                    endDate = LocalDate.of(2026, 4, 30),
                ),
        )

    val timeTable =
        listOf(
            ScreeningSchedule(
                movie = adventureOfBard,
                screenTime =
                    ScreenTime(
                        startTime = LocalTime.of(9, 0),
                        endTime = LocalTime.of(11, 0),
                        screeningDate = LocalDate.of(2026, 4, 5),
                    ),
            ),
            ScreeningSchedule(
                movie = adventureOfBard,
                screenTime =
                    ScreenTime(
                        startTime = LocalTime.of(11, 0),
                        endTime = LocalTime.of(13, 0),
                        screeningDate = LocalDate.of(2026, 4, 5),
                    ),
            ),
            ScreeningSchedule(
                movie = adventureOfBard,
                screenTime =
                    ScreenTime(
                        startTime = LocalTime.of(13, 0),
                        endTime = LocalTime.of(15, 0),
                        screeningDate = LocalDate.of(2026, 4, 5),
                    ),
            ),
            ScreeningSchedule(
                movie = adventureOfBard,
                screenTime =
                    ScreenTime(
                        startTime = LocalTime.of(15, 0),
                        endTime = LocalTime.of(17, 0),
                        screeningDate = LocalDate.of(2026, 4, 5),
                    ),
            ),
            ScreeningSchedule(
                movie = adventureOfBard,
                screenTime =
                    ScreenTime(
                        startTime = LocalTime.of(17, 0),
                        endTime = LocalTime.of(19, 0),
                        screeningDate = LocalDate.of(2026, 4, 5),
                    ),
            ),
            ScreeningSchedule(
                movie = adventureOfBard,
                screenTime =
                    ScreenTime(
                        startTime = LocalTime.of(19, 0),
                        endTime = LocalTime.of(21, 0),
                        screeningDate = LocalDate.of(2026, 4, 5),
                    ),
            ),
            ScreeningSchedule(
                movie = adventureOfBard,
                screenTime =
                    ScreenTime(
                        startTime = LocalTime.of(9, 0),
                        endTime = LocalTime.of(11, 0),
                        screeningDate = LocalDate.of(2026, 4, 10),
                    ),
            ),
            ScreeningSchedule(
                movie = adventureOfBard,
                screenTime =
                    ScreenTime(
                        startTime = LocalTime.of(11, 0),
                        endTime = LocalTime.of(13, 0),
                        screeningDate = LocalDate.of(2026, 4, 10),
                    ),
            ),
            ScreeningSchedule(
                movie = adventureOfBard,
                screenTime =
                    ScreenTime(
                        startTime = LocalTime.of(13, 0),
                        endTime = LocalTime.of(15, 0),
                        screeningDate = LocalDate.of(2026, 4, 10),
                    ),
            ),
            ScreeningSchedule(
                movie = adventureOfBard,
                screenTime =
                    ScreenTime(
                        startTime = LocalTime.of(15, 0),
                        endTime = LocalTime.of(17, 0),
                        screeningDate = LocalDate.of(2026, 4, 10),
                    ),
            ),
            ScreeningSchedule(
                movie = adventureOfBard,
                screenTime =
                    ScreenTime(
                        startTime = LocalTime.of(17, 0),
                        endTime = LocalTime.of(19, 0),
                        screeningDate = LocalDate.of(2026, 4, 10),
                    ),
            ),
            ScreeningSchedule(
                movie = adventureOfBard,
                screenTime =
                    ScreenTime(
                        startTime = LocalTime.of(19, 0),
                        endTime = LocalTime.of(21, 0),
                        screeningDate = LocalDate.of(2026, 4, 10),
                    ),
            ),
            ScreeningSchedule(
                movie = adventureOfBard,
                screenTime =
                    ScreenTime(
                        startTime = LocalTime.of(9, 0),
                        endTime = LocalTime.of(11, 0),
                        screeningDate = LocalDate.of(2026, 4, 15),
                    ),
            ),
            ScreeningSchedule(
                movie = adventureOfBard,
                screenTime =
                    ScreenTime(
                        startTime = LocalTime.of(11, 0),
                        endTime = LocalTime.of(13, 0),
                        screeningDate = LocalDate.of(2026, 4, 15),
                    ),
            ),
            ScreeningSchedule(
                movie = adventureOfBard,
                screenTime =
                    ScreenTime(
                        startTime = LocalTime.of(13, 0),
                        endTime = LocalTime.of(15, 0),
                        screeningDate = LocalDate.of(2026, 4, 15),
                    ),
            ),
            ScreeningSchedule(
                movie = adventureOfBard,
                screenTime =
                    ScreenTime(
                        startTime = LocalTime.of(15, 0),
                        endTime = LocalTime.of(17, 0),
                        screeningDate = LocalDate.of(2026, 4, 15),
                    ),
            ),
            ScreeningSchedule(
                movie = adventureOfBard,
                screenTime =
                    ScreenTime(
                        startTime = LocalTime.of(17, 0),
                        endTime = LocalTime.of(19, 0),
                        screeningDate = LocalDate.of(2026, 4, 15),
                    ),
            ),
            ScreeningSchedule(
                movie = adventureOfBard,
                screenTime =
                    ScreenTime(
                        startTime = LocalTime.of(19, 0),
                        endTime = LocalTime.of(21, 0),
                        screeningDate = LocalDate.of(2026, 4, 15),
                    ),
            ),
            ScreeningSchedule(
                movie = ironMan,
                screenTime =
                    ScreenTime(
                        startTime = LocalTime.of(9, 0),
                        endTime = LocalTime.of(11, 0),
                        screeningDate = LocalDate.of(2026, 4, 5),
                    ),
            ),
            ScreeningSchedule(
                movie = ironMan,
                screenTime =
                    ScreenTime(
                        startTime = LocalTime.of(11, 0),
                        endTime = LocalTime.of(13, 0),
                        screeningDate = LocalDate.of(2026, 4, 5),
                    ),
            ),
            ScreeningSchedule(
                movie = ironMan,
                screenTime =
                    ScreenTime(
                        startTime = LocalTime.of(13, 0),
                        endTime = LocalTime.of(15, 0),
                        screeningDate = LocalDate.of(2026, 4, 5),
                    ),
            ),
            ScreeningSchedule(
                movie = ironMan,
                screenTime =
                    ScreenTime(
                        startTime = LocalTime.of(15, 0),
                        endTime = LocalTime.of(17, 0),
                        screeningDate = LocalDate.of(2026, 4, 5),
                    ),
            ),
            ScreeningSchedule(
                movie = ironMan,
                screenTime =
                    ScreenTime(
                        startTime = LocalTime.of(17, 0),
                        endTime = LocalTime.of(19, 0),
                        screeningDate = LocalDate.of(2026, 4, 5),
                    ),
            ),
            ScreeningSchedule(
                movie = ironMan,
                screenTime =
                    ScreenTime(
                        startTime = LocalTime.of(19, 0),
                        endTime = LocalTime.of(21, 0),
                        screeningDate = LocalDate.of(2026, 4, 5),
                    ),
            ),
            ScreeningSchedule(
                movie = ironMan,
                screenTime =
                    ScreenTime(
                        startTime = LocalTime.of(9, 0),
                        endTime = LocalTime.of(11, 0),
                        screeningDate = LocalDate.of(2026, 4, 10),
                    ),
            ),
            ScreeningSchedule(
                movie = ironMan,
                screenTime =
                    ScreenTime(
                        startTime = LocalTime.of(11, 0),
                        endTime = LocalTime.of(13, 0),
                        screeningDate = LocalDate.of(2026, 4, 10),
                    ),
            ),
            ScreeningSchedule(
                movie = ironMan,
                screenTime =
                    ScreenTime(
                        startTime = LocalTime.of(13, 0),
                        endTime = LocalTime.of(15, 0),
                        screeningDate = LocalDate.of(2026, 4, 10),
                    ),
            ),
            ScreeningSchedule(
                movie = ironMan,
                screenTime =
                    ScreenTime(
                        startTime = LocalTime.of(15, 0),
                        endTime = LocalTime.of(17, 0),
                        screeningDate = LocalDate.of(2026, 4, 10),
                    ),
            ),
            ScreeningSchedule(
                movie = ironMan,
                screenTime =
                    ScreenTime(
                        startTime = LocalTime.of(17, 0),
                        endTime = LocalTime.of(19, 0),
                        screeningDate = LocalDate.of(2026, 4, 10),
                    ),
            ),
            ScreeningSchedule(
                movie = ironMan,
                screenTime =
                    ScreenTime(
                        startTime = LocalTime.of(19, 0),
                        endTime = LocalTime.of(21, 0),
                        screeningDate = LocalDate.of(2026, 4, 10),
                    ),
            ),
            ScreeningSchedule(
                movie = ironMan,
                screenTime =
                    ScreenTime(
                        startTime = LocalTime.of(9, 0),
                        endTime = LocalTime.of(11, 0),
                        screeningDate = LocalDate.of(2026, 4, 15),
                    ),
            ),
            ScreeningSchedule(
                movie = ironMan,
                screenTime =
                    ScreenTime(
                        startTime = LocalTime.of(11, 0),
                        endTime = LocalTime.of(13, 0),
                        screeningDate = LocalDate.of(2026, 4, 15),
                    ),
            ),
            ScreeningSchedule(
                movie = ironMan,
                screenTime =
                    ScreenTime(
                        startTime = LocalTime.of(13, 0),
                        endTime = LocalTime.of(15, 0),
                        screeningDate = LocalDate.of(2026, 4, 15),
                    ),
            ),
            ScreeningSchedule(
                movie = ironMan,
                screenTime =
                    ScreenTime(
                        startTime = LocalTime.of(15, 0),
                        endTime = LocalTime.of(17, 0),
                        screeningDate = LocalDate.of(2026, 4, 15),
                    ),
            ),
            ScreeningSchedule(
                movie = ironMan,
                screenTime =
                    ScreenTime(
                        startTime = LocalTime.of(17, 0),
                        endTime = LocalTime.of(19, 0),
                        screeningDate = LocalDate.of(2026, 4, 15),
                    ),
            ),
            ScreeningSchedule(
                movie = ironMan,
                screenTime =
                    ScreenTime(
                        startTime = LocalTime.of(19, 0),
                        endTime = LocalTime.of(21, 0),
                        screeningDate = LocalDate.of(2026, 4, 15),
                    ),
            ),
        )
}
