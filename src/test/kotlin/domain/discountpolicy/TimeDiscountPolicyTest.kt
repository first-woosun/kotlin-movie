package domain.discountpolicy

import movie.domain.discountpolicy.MovieDayDiscountPolicy
import movie.domain.discountpolicy.TimeDiscountPolicy
import movie.domain.money.Money
import movie.domain.timetable.items.ScreenTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime

class TimeDiscountPolicyTest {
    private val timeDiscountPolicy = TimeDiscountPolicy(discountAmount = Money(2000))
    private val movieDayDiscountPolicy = MovieDayDiscountPolicy(discountRate = 0.9)
    private val baseAmount = Money(10000)

    @Test
    fun `예매의 시간이 11시 이전이면 2000원 할인된다`() {
        val screenTime = createScreenTime(startTime = LocalTime.of(10, 0))

        val result = timeDiscountPolicy.applyDiscount(baseAmount, screenTime)

        assertThat(result).isEqualTo(Money(8000))
    }

    @Test
    fun `예매의 시간이 8시 이후면 2000원 할인된다`() {
        val screenTime = createScreenTime(startTime = LocalTime.of(20, 0))

        val result = timeDiscountPolicy.applyDiscount(baseAmount, screenTime)

        assertThat(result).isEqualTo(Money(8000))
    }

    @Test
    fun `예매의 시간이 11시부터 20시 사이면 할인이 적용되지 않는다`() {
        val screenTime = createScreenTime(startTime = LocalTime.of(13, 0))

        val result = timeDiscountPolicy.applyDiscount(baseAmount, screenTime)

        assertThat(result).isEqualTo(Money(10000))
    }

    @Test
    fun `예매의 일자가 10일, 20일, 30일 중 하나면 10% 할인된다`() {
        val screenTime = createScreenTime(screeningDate = LocalDate.of(2026, 4, 10))

        val result = movieDayDiscountPolicy.applyDiscount(baseAmount, screenTime)

        assertThat(result).isEqualTo(Money(9000))
    }

    @Test
    fun `예매의 일자가 10일, 20일, 30일 중 하나가 아니면 할인되지 않는다`() {
        val screenTime = createScreenTime(screeningDate = LocalDate.of(2026, 4, 15))

        val result = movieDayDiscountPolicy.applyDiscount(baseAmount, screenTime)

        assertThat(result).isEqualTo(Money(10000))
    }

    private fun createScreenTime(
        startTime: LocalTime = LocalTime.of(10, 0),
        endTime: LocalTime = LocalTime.of(12, 0),
        screeningDate: LocalDate = LocalDate.of(2026, 4, 10),
    ) = ScreenTime(
        startTime = startTime,
        endTime = endTime,
        screeningDate = screeningDate,
    )
}
