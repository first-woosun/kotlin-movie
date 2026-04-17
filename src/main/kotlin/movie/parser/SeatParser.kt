package movie.parser

import movie.domain.seat.Seat
import movie.domain.seat.items.ColumnNumber
import movie.domain.seat.items.RowNumber
import movie.view.input.InputView

object SeatParser {
    fun parse(input: String): List<Seat> {
        val seats = mutableListOf<Seat>()
        val numbers = input.split(",").map { it.trim() }
        numbers.forEach {
            require(it.matches(Regex("^[A-E][1-4]$"))) { InputView.LABEL.INVALID_SEAT_NUMBER_FORMAT_ERROR }
            val rowNumber = RowNumber(it[0].toString())
            val columnNumber = ColumnNumber(it[1].digitToInt())
            seats.add(
                Seat.Companion.create(rowNumber, columnNumber),
            )
        }
        return seats
    }
}
