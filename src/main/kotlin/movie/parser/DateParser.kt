package movie.parser

import java.time.LocalDate

object DateParser {
    fun parse(input: String): LocalDate =
        try {
            LocalDate.parse(input)
        } catch (e: Exception) {
            throw IllegalArgumentException("입력 형식이 올바르지 않습니다.")
        }
}
