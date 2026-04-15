package domain.seat

import domain.money.Money
import domain.seat.items.ColumnNumber
import domain.seat.items.RowNumber
import domain.seat.items.SeatGrade

class Seat(
    seatNumber: String,
) {
    private val rowNumber: RowNumber
    private val columnNumber: ColumnNumber
    private val seatGrade: SeatGrade

    init {
        val row = seatNumber.substring(0, 1)
        val col = seatNumber.substring(1).toInt()
        rowNumber = RowNumber(row)
        columnNumber = ColumnNumber(col)
        seatGrade =
            when (row) {
                "A", "B" -> SeatGrade.GradeB
                "C", "D" -> SeatGrade.GradeS
                "E" -> SeatGrade.GradeA
                else -> throw IllegalArgumentException("존재하지 않는 좌석입니다.")
            }
    }

    fun isExist(number: String): Boolean {
        val row = number.substring(0, 1)
        val col = number.substring(1).toInt()
        return columnNumber.isSame(col) && rowNumber.isSame(row)
    }

    fun getPrice(): Money = seatGrade.price

    fun getSeatNumber(): String = rowNumber.getRow() + columnNumber.getColum()
}
