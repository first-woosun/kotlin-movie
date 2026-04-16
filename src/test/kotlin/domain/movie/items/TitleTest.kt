package domain.movie.items

import domain.movie.itmes.Title
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class TitleTest {
    @Test
    fun `영화의 이름은 빈 문자열이 예외를 던진다`() {
        assertThrows<IllegalArgumentException> { Title("") }
    }
}
