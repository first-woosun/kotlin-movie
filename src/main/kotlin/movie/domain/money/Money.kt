package movie.domain.money

import movie.domain.discountpolicy.PayMethodDiscountPolicy

@JvmInline
value class Money(
    val amount: Int,
) {
    init {
        require(amount >= 0) { "가격은 0보다 작을 수 없습니다. (입력값: $amount)" }
    }

    fun applyPoint(pointAmount: Int): Money = Money(amount - pointAmount)

    fun applyPayMethod(payMethodDiscountPolicy: PayMethodDiscountPolicy): Money = payMethodDiscountPolicy.applyDiscount(this)

    operator fun plus(other: Money): Money = Money(amount + other.amount)

    operator fun minus(other: Money): Money = Money(amount - other.amount)

    operator fun times(scale: Double): Money = Money((amount * scale).toInt())

    operator fun compareTo(other: Money): Int = this.amount.compareTo(other.amount)
}
