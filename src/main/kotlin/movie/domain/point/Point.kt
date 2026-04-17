package movie.domain.point

@JvmInline
value class Point(
    val amount: Int,
) {
    init {
        require(amount >= 0) { "포인트는 0보다 작을 수 없습니다. (입력값: $amount)" }
    }

    fun use(amount: Int): Point {
        require(amount >= 0) { "사용할 포인트는 0보다 작을 수 없습니다." }

        require(amount <= this.amount) { "잔여 포인트보다 많은 포인트를 사용할 수 없습니다." }

        return Point(this.amount - amount)
    }

    fun isBiggerThan(price: Int): Boolean = amount > price
}
