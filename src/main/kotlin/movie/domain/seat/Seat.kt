package movie.domain.seat

import movie.domain.money.Money
import movie.domain.seat.items.ColumnNumber
import movie.domain.seat.items.RowNumber
import movie.domain.seat.items.SeatGrade

data class Seat(
    val rowNumber: RowNumber,
    val columnNumber: ColumnNumber,
    private val seatGrade: SeatGrade,
) {
    fun isExist(findingSeat: Seat): Boolean = this == findingSeat

    fun getPrice(): Money = seatGrade.price

    fun getSeatNumber(): String = rowNumber.rowNumber + columnNumber.columnNumber

    companion object {
        fun create(
            rowNumber: RowNumber,
            columnNumber: ColumnNumber,
        ): Seat =
            Seat(
                rowNumber = rowNumber,
                columnNumber = columnNumber,
                seatGrade = SeatGrade.Companion.from(rowNumber),
            )
    }
}
