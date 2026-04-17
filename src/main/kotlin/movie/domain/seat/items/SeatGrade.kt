package movie.domain.seat.items

import movie.domain.money.Money

enum class SeatGrade(
    val price: Money,
) {
    GradeS(Money(18000)),
    GradeA(Money(15000)),
    GradeB(Money(13000)),
    ;

    companion object {
        fun from(row: RowNumber): SeatGrade =
            when (row) {
                RowNumber("A"), RowNumber("B") -> GradeB
                RowNumber("C"), RowNumber("D") -> GradeS
                else -> GradeA
            }
    }
}
