package movie.domain.seat.items

@JvmInline
value class RowNumber(
    val rowNumber: String,
) {
    fun isSame(number: String): Boolean = rowNumber == number
}
