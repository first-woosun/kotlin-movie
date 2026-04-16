package domain.discountpolicy

import domain.money.Money

interface PayMethodDiscountPolicy {
    fun applyDiscount(price: Money): Money
}

class CardDiscountPolicy(
    private val discountRate: Double,
) : PayMethodDiscountPolicy {
    override fun applyDiscount(price: Money): Money = price * discountRate
}

class CashDiscountPolicy(
    private val discountRate: Double,
) : PayMethodDiscountPolicy {
    override fun applyDiscount(price: Money): Money = price * discountRate
}
