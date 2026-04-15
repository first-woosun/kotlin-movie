package domain.seat

import domain.money.Money
import domain.seat.items.ColumnNumber
import domain.seat.items.RowNumber
import domain.seat.items.SeatGrade

class Seat(
    private val rowNumber: RowNumber,
    private val columnNumber: ColumnNumber,
    private val seatGrade: SeatGrade,
) {
    fun isExist(number: String): Boolean {
        val row = number.substring(0, 1)
        val col = number.substring(1).toInt()
        return columnNumber.isSame(col) && rowNumber.isSame(row)
    }

    fun getPrice(): Money = seatGrade.price

    fun getSeatNumber(): String = rowNumber.rowNumber + columnNumber.columnNumber
}
