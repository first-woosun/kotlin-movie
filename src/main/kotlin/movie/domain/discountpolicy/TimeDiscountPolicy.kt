package movie.domain.discountpolicy

import movie.domain.money.Money
import movie.domain.timetable.items.ScreenTime
import java.time.LocalTime

interface DiscountPolicy {
    fun applyDiscount(
        price: Money,
        screenTime: ScreenTime,
    ): Money
}

class TimeDiscountPolicy(
    private val discountAmount: Money,
) : DiscountPolicy {
    private val timeCondition =
        listOf(
            LocalTime.of(11, 0),
            LocalTime.of(20, 0),
        )

    override fun applyDiscount(
        price: Money,
        screenTime: ScreenTime,
    ): Money {
        if (screenTime.isStartBefore(timeCondition[0]) || screenTime.isStartAfter(timeCondition[1])) {
            return price - discountAmount
        }
        return price
    }
}

class MovieDayDiscountPolicy(
    private val discountRate: Double,
) : DiscountPolicy {
    private val movieDay = listOf(10, 20, 30)

    override fun applyDiscount(
        price: Money,
        screenTime: ScreenTime,
    ): Money {
        movieDay.forEach {
            if (screenTime.isSameDate(it)) return price * discountRate
        }
        return price
    }
}
