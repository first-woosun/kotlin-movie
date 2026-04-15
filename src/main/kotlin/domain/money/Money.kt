package domain.money

import domain.discountpolicy.PayMethodDiscountPolicy
import domain.point.Point

@JvmInline
value class Money(
    val amount: Int,
) {
    init {
        require(amount >= 0) { "가격은 0보다 작을 수 없습니다. (입력값: $amount)" }
    }

    fun getAmount() = amount

    fun applyPoint(pointAmount: Int): Money {
        return Money(amount - pointAmount)
    }

    fun applyPayMethod(payMethodDiscountPolicy: PayMethodDiscountPolicy): Money {
        return payMethodDiscountPolicy.applyDiscount(this)
    }

    operator fun plus(other: Money): Money = Money(amount + other.amount)

    operator fun minus(other: Money): Money = Money(amount - other.amount)

    operator fun times(scale: Double): Money = Money((amount * scale).toInt())

    operator fun compareTo(other: Money): Int = this.amount.compareTo(other.amount)
}
