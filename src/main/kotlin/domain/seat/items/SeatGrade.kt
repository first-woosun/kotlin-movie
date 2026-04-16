package domain.seat.items

import domain.money.Money

enum class SeatGrade(
    val price: Money,
) {
    GradeS(Money(18000)),
    GradeA(Money(15000)),
    GradeB(Money(13000)),
}
