package domain.reservations.items

import movie.domain.movie.Movie
import movie.domain.movie.itmes.RunningTime
import movie.domain.movie.itmes.ScreeningPeriod
import movie.domain.movie.itmes.Title
import movie.domain.reservations.items.Reservation
import movie.domain.seat.Seat
import movie.domain.seat.items.ColumnNumber
import movie.domain.seat.items.RowNumber
import movie.domain.timetable.items.ScreenTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime

class ReservationTest {
    private val reservation = createReservation()

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

    private fun createReservation() =
        Reservation(
            movie = createMovie(),
            screenTime = createScreenTime(),
            seats =
                listOf(
                    Seat.create(RowNumber("A"), ColumnNumber(1)),
                    Seat.create(RowNumber("B"), ColumnNumber(1)),
                ),
        )

    private fun createMovie() =
        Movie(
            title = Title("신바드의 모험"),
            runningTime = RunningTime(120),
            screeningPeriod =
                ScreeningPeriod(
                    startDate = LocalDate.of(2026, 4, 1),
                    endDate = LocalDate.of(2026, 4, 30),
                ),
        )

    private fun createScreenTime() =
        ScreenTime(
            startTime = LocalTime.of(11, 0),
            endTime = LocalTime.of(13, 0),
            screeningDate = LocalDate.of(2026, 4, 10),
        )
}
