package domain.discountpolicy

import domain.money.Money
import domain.movie.Movie
import domain.movie.itmes.RunningTime
import domain.movie.itmes.ScreeningPeriod
import domain.movie.itmes.Title
import domain.reservations.items.Reservation
import domain.seat.Seat
import domain.seat.items.ColumnNumber
import domain.seat.items.RowNumber
import domain.seat.items.SeatGrade
import domain.timetable.items.ScreenTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime

class TimeDiscountPolicyTest {
    val timeDiscountPolicy =
        TimeDiscountPolicy(
            discountAmount = Money(2000),
        )
    val movieDayDiscountPolicy =
        MovieDayDiscountPolicy(
            discountRate = 0.9,
        )

    @Test
    fun `예매의 시간이 11시 이전이면 2000원 할인된다`() {
        val amount = Money(10000)

        val screenTime = ScreenTime(
            startTime =
                LocalTime.of(
                    10,
                    0,
                ),
            endTime =
                LocalTime.of(
                    12,
                    0,
                ),
            screeningDate =
                LocalDate.of(
                    2026,
                    4,
                    10,
                ),
        )

        val result = timeDiscountPolicy.applyDiscount(amount, screenTime)

        assertThat(result).isEqualTo(Money(8000))
    }

    @Test
    fun `예매의 시간이 8시 이후면 2000원 할인된다`() {
        val amount = Money(10000)

        val screenTime = ScreenTime(
            startTime =
                LocalTime.of(
                    20,
                    0,
                ),
            endTime =
                LocalTime.of(
                    22,
                    0,
                ),
            screeningDate =
                LocalDate.of(
                    2026,
                    4,
                    10,
                ),
        )

        val result = timeDiscountPolicy.applyDiscount(amount, screenTime)

        assertThat(result).isEqualTo(Money(8000))
    }

    @Test
    fun `예매의 시간이 11시부터 20시 사이면 할인이 적용되지 않는다`() {
        val amount = Money(10000)

        val screenTime = ScreenTime(
            startTime =
                LocalTime.of(
                    13,
                    0,
                ),
            endTime =
                LocalTime.of(
                    15,
                    0,
                ),
            screeningDate =
                LocalDate.of(
                    2026,
                    4,
                    10,
                ),
        )

        val result = timeDiscountPolicy.applyDiscount(amount, screenTime)

        assertThat(result).isEqualTo(Money(10000))
    }

    @Test
    fun `예매의 일자가 10일, 20일, 30일 중 하나면 10% 할인된다`() {
        val amount = Money(10000)

        val screenTime = ScreenTime(
            startTime =
                LocalTime.of(
                    20,
                    0,
                ),
            endTime =
                LocalTime.of(
                    22,
                    0,
                ),
            screeningDate =
                LocalDate.of(
                    2026,
                    4,
                    10,
                ),
        )

        val result = movieDayDiscountPolicy.applyDiscount(amount, screenTime)

        assertThat(result).isEqualTo(Money(9000))
    }

    @Test
    fun `예매의 일자가 10일, 20일, 30일 중 하나가 아니면 할인되지 않는다`() {
        val amount = Money(10000)

        val screenTime = ScreenTime(
            startTime =
                LocalTime.of(
                    20,
                    0,
                ),
            endTime =
                LocalTime.of(
                    22,
                    0,
                ),
            screeningDate =
                LocalDate.of(
                    2026,
                    4,
                    15,
                ),
        )

        val result = movieDayDiscountPolicy.applyDiscount(amount, screenTime)

        assertThat(result).isEqualTo(Money(10000))
    }
}
