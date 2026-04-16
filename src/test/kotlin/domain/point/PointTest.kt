package domain.point

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class PointTest {
    @Test
    fun `포인트 초기값이 0보다 작으면 예외가 발생한다`() {
        assertThrows<IllegalArgumentException> { Point(-1) }
    }

    @Test
    fun `사용할 포인트를 입력 받을 때 입력받은 값이 0보다 작으면 예외가 발생한다`() {
        val point = Point(0)
        assertThrows<IllegalArgumentException> { point.use(-1) }
    }

    @Test
    fun `사용할 포인트를 입력 받을 때 입력받은 값이 잔여 포인트보다 많으면 예외가 발생한다`() {
        val point = Point(0)
        assertThrows<IllegalArgumentException> { point.use(1) }
    }

    @Test
    fun `입력받은 값이 0보다 크고 잔여 포인트보다 작으면 잔여 포인트에서 입력값 만큼 차감한다`() {
        val point = Point(100)

        val result = point.use(10)

        assertThat(result).isEqualTo(Point(90))
    }
}
