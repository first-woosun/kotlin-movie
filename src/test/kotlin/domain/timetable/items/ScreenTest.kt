package domain.timetable.items

import domain.seat.Seat
import domain.seat.items.ColumnNumber
import domain.seat.items.RowNumber
import domain.seat.items.SeatGrade
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ScreenTest {
    val seats =
        listOf(
            Seat(
                rowNumber = RowNumber("A"),
                columnNumber = ColumnNumber(1),
                seatGrade = SeatGrade.GradeA
            ),
            Seat(
                rowNumber = RowNumber("A"),
                columnNumber = ColumnNumber(2),
                seatGrade = SeatGrade.GradeA
            ),
            Seat(
                rowNumber = RowNumber("A"),
                columnNumber = ColumnNumber(3),
                seatGrade = SeatGrade.GradeA
            ),
            Seat(
                rowNumber = RowNumber("A"),
                columnNumber = ColumnNumber(4),
                seatGrade = SeatGrade.GradeA
            ),
            Seat(rowNumber = RowNumber("A"),
                columnNumber = ColumnNumber(5),
                seatGrade = SeatGrade.GradeA),
        )

    @Test
    fun `입력된 좌석 번호가 상영관에 존재하는 좌석이면 true가 반환된다`() {
        val screen =
            Screen(
                seats = seats,
            )

        val result = screen.findSeat("A1")
        assertThat(result).isTrue()
    }

    @Test
    fun `입력된 좌석 번호가 상영관에 존재하지 않는 좌석이면 false가 반환된다`() {
        val screen =
            Screen(
                seats = seats,
            )

        val result = screen.findSeat("F1")
        assertThat(result).isFalse()
    }
}
