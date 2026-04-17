package movie.domain.discountpolicy

enum class PayMethod {
    CARD,
    CASH,
    ;

    companion object {
        fun toPolicy(
            payMethod: PayMethod,
            cardDiscountPolicy: CardDiscountPolicy,
            cashDiscountPolicy: CashDiscountPolicy,
        ): PayMethodDiscountPolicy =
            when (payMethod) {
                CARD -> cardDiscountPolicy
                CASH -> cashDiscountPolicy
            }
    }
}
