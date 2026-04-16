package domain.seat

import domain.money.Money
import domain.seat.items.ColumnNumber
import domain.seat.items.RowNumber
import domain.seat.items.SeatGrade

data class Seat(
    private val rowNumber: RowNumber,
    private val columnNumber: ColumnNumber,
    private val seatGrade: SeatGrade,
) {
    fun isExist(findingSeat: Seat): Boolean {
        return this == findingSeat
    }

    fun getPrice(): Money = seatGrade.price

    fun getSeatNumber(): String = rowNumber.rowNumber + columnNumber.columnNumber

    companion object {
        fun create(rowNumber: RowNumber, columnNumber: ColumnNumber): Seat {
            return Seat(
                rowNumber = rowNumber,
                columnNumber = columnNumber,
                seatGrade = SeatGrade.from(rowNumber)
            )
        }
    }
}
