package movie.domain.timetable.items

import movie.domain.seat.Seat

class Screen(
    private val seats: List<Seat>,
) {
    fun findSeat(seat: Seat): Boolean = seats.any { it.isExist(seat) }

    companion object {
        val seatMap =
            mapOf(
                "A" to listOf("B", "B", "B", "B"),
                "B" to listOf("B", "B", "B", "B"),
                "C" to listOf("S", "S", "S", "S"),
                "D" to listOf("S", "S", "S", "S"),
                "E" to listOf("A", "A", "A", "A"),
            )
    }
}
