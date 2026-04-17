package movie.domain.seat.items

@JvmInline
value class ColumnNumber(
    val columnNumber: Int,
) {
    fun isSame(number: Int) = columnNumber == number
}
