import controller.Controller
import domain.discountpolicy.CardDiscountPolicy
import domain.discountpolicy.CashDiscountPolicy
import domain.discountpolicy.MovieDayDiscountPolicy
import domain.discountpolicy.TimeDiscountPolicy
import domain.money.Money
import view.input.InputView
import view.output.OutputView

fun main() {
    val timeDiscountPolicy =
        TimeDiscountPolicy(
            discountAmount = Money(2000),
        )
    val movieDayDiscountPolicy =
        MovieDayDiscountPolicy(
            discountRate = 0.9,
        )

    val cardDiscountPolicy = CardDiscountPolicy(discountRate = 0.95)

    val cashDiscountPolicy = CashDiscountPolicy(0.98)

    Controller(
        inputView = InputView,
        outputView = OutputView,
        cardDiscountPolicy = cardDiscountPolicy,
        cashDiscountPolicy = cashDiscountPolicy,
        timeDiscountPolicy = timeDiscountPolicy,
        movieDayDiscountPolicy = movieDayDiscountPolicy,
    ).run()
}
