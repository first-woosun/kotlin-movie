package domain.timetable.items

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime

class ScreenTimeTest {
    @Test
    fun `입력받은 시간이 startTime과 endTime 사이에 있다면 true를 반환한다`() {
        val screenTime =
            ScreenTime(
                startTime = LocalTime.of(11, 0),
                endTime = LocalTime.of(13, 0),
                screeningDate = LocalDate.of(2026, 4, 10),
            )

        val result = screenTime.isContain(LocalTime.of(12, 0))

        assertThat(result).isTrue()
    }

    @Test
    fun `입력된 시작 시간이 소유하고 있는 시작 시간과 같다면 true를 반환한다`() {
        val screenTime =
            ScreenTime(
                startTime = LocalTime.of(11, 0),
                endTime = LocalTime.of(13, 0),
                screeningDate = LocalDate.of(2026, 4, 10),
            )

        val result = screenTime.isStartAt(LocalTime.of(11, 0))

        assertThat(result).isTrue()
    }

    @Test
    fun `입력된 시작 시간이 소유하고 있는 시작 시간과 다르다면 false를 반환한다`() {
        val screenTime =
            ScreenTime(
                startTime = LocalTime.of(11, 0),
                endTime = LocalTime.of(13, 0),
                screeningDate = LocalDate.of(2026, 4, 10),
            )

        val result = screenTime.isStartAt(LocalTime.of(12, 0))

        assertThat(result).isFalse()
    }

    @Test
    fun `입력받은 상영 날자가 소유하고 있는 상영 날자와 같다면 true를 반환한다`() {
        val screenTime =
            ScreenTime(
                startTime = LocalTime.of(11, 0),
                endTime = LocalTime.of(13, 0),
                screeningDate = LocalDate.of(2026, 4, 10),
            )

        val result = screenTime.isScreeningAt(LocalDate.of(2026, 4, 10))

        assertThat(result).isTrue()
    }

    @Test
    fun `입력받은 상영 날자가 소유하고 있는 상영 날자와 다르다면 false를 반환한다`() {
        val screenTime =
            ScreenTime(
                startTime = LocalTime.of(11, 0),
                endTime = LocalTime.of(13, 0),
                screeningDate = LocalDate.of(2026, 4, 10),
            )

        val result = screenTime.isScreeningAt(LocalDate.of(2026, 4, 1))

        assertThat(result).isFalse()
    }
}
