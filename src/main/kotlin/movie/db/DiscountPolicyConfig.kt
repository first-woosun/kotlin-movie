package movie.db

import movie.domain.discountpolicy.CardDiscountPolicy
import movie.domain.discountpolicy.CashDiscountPolicy
import movie.domain.discountpolicy.MovieDayDiscountPolicy
import movie.domain.discountpolicy.TimeDiscountPolicy
import movie.domain.money.Money
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DiscountPolicyConfig {
    @Bean
    fun timeDiscountPolicy() = TimeDiscountPolicy(Money(2000))

    @Bean
    fun movieDayDiscountPolicy() = MovieDayDiscountPolicy(0.9)

    @Bean
    fun cardDiscountPolicy() = CardDiscountPolicy(0.95)

    @Bean
    fun cashDiscountPolicy() = CashDiscountPolicy(0.98)
}
