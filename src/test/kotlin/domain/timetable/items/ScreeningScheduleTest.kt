package domain.timetable.items

import domain.movie.Movie
import domain.movie.itmes.RunningTime
import domain.movie.itmes.ScreeningPeriod
import domain.movie.itmes.Title
import domain.seat.Seat
import domain.seat.items.ColumnNumber
import domain.seat.items.RowNumber
import domain.seat.items.SeatGrade
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime

class ScreeningScheduleTest {
    val schedule =
        ScreeningSchedule(
            Movie(
                title = Title("신바드의 모험"),
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
            screenTime =
                ScreenTime(
                    startTime =
                        LocalTime.of(
                            11,
                            0,
                        ),
                    endTime =
                        LocalTime.of(
                            13,
                            0,
                        ),
                    screeningDate =
                        LocalDate.of(
                            2026,
                            4,
                            10,
                        ),
                ),
        )

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
        val result =
            schedule.isScreeningDate(
                LocalDate.of(
                    2026,
                    4,
                    10,
                ),
            )

        assertThat(result).isTrue()
    }

    @Test
    fun `입력된 상영 일자가 소유하고 있는 영화 상영 일자와 다르다면 false를 반환받는다`() {
        val result =
            schedule.isScreeningDate(
                LocalDate.of(
                    2026,
                    4,
                    1,
                ),
            )

        assertThat(result).isFalse()
    }

    @Test
    fun `입력된 좌석 번호가 reservedSeats에 있다면 true를 반환한다`() {
        val reservationSchedule =
            ScreeningSchedule(
                Movie(
                    title = Title("신바드의 모험"),
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
                screenTime =
                    ScreenTime(
                        startTime =
                            LocalTime.of(
                                11,
                                0,
                            ),
                        endTime =
                            LocalTime.of(
                                13,
                                0,
                            ),
                        screeningDate =
                            LocalDate.of(
                                2026,
                                4,
                                10,
                            ),
                    ),
            )
        val reserveSeat =
            Seat(
                rowNumber = RowNumber("A"),
                columnNumber = ColumnNumber(1),
                seatGrade = SeatGrade.GradeA,
            )
        reservationSchedule.reserveSeat(reserveSeat)

        val result = reservationSchedule.isReservedSeat("A1")

        assertThat(result).isTrue()
    }

    @Test
    fun `입력된 좌석 번호가 reservedSeats에 없다면 false를 반환한다`() {
        val reservationSchedule =
            ScreeningSchedule(
                Movie(
                    title = Title("신바드의 모험"),
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
                screenTime =
                    ScreenTime(
                        startTime =
                            LocalTime.of(
                                11,
                                0,
                            ),
                        endTime =
                            LocalTime.of(
                                13,
                                0,
                            ),
                        screeningDate =
                            LocalDate.of(
                                2026,
                                4,
                                10,
                            ),
                    ),
            )
        val reserveSeat =
            Seat(
                rowNumber = RowNumber("A"),
                columnNumber = ColumnNumber(1),
                seatGrade = SeatGrade.GradeA
            )
        reservationSchedule.reserveSeat(reserveSeat)

        val result = reservationSchedule.isReservedSeat("B1")

        assertThat(result).isFalse()
    }
}
