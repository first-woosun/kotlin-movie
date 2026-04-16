package parser

import domain.seat.Seat
import domain.seat.items.ColumnNumber
import domain.seat.items.RowNumber
import view.input.InputView.LABEL

object SeatParser {
    fun parse(input: String): List<Seat> {
        val seats = mutableListOf<Seat>()
        val numbers = input.split(",").map { it.trim() }
        numbers.forEach {
            require(it.matches(Regex("^[A-E][1-4]$"))) { LABEL.INVALID_SEAT_NUMBER_FORMAT_ERROR }
            val rowNumber = RowNumber(it[0].toString())
            val columnNumber = ColumnNumber(it[1].digitToInt())
            seats.add(
                Seat.create(rowNumber, columnNumber),
            )
        }
        return seats
    }
}
