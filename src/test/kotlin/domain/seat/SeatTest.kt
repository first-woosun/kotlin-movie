package domain.seat

import domain.seat.items.ColumnNumber
import domain.seat.items.RowNumber
import domain.seat.items.SeatGrade
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SeatTest {
    @Test
    fun `입력된 좌석 번호가 저장된 좌석 번호와 일치하면 true를 반환한다`() {
        val seat =
            Seat(
                rowNumber = RowNumber("A"),
                columnNumber = ColumnNumber(1),
                seatGrade = SeatGrade.GradeB,
            )

        val result = seat.isExist("A1")

        assertThat(result).isTrue()
    }

    @Test
    fun `입력된 좌석 번호가 저장된 좌석 번호와 일치하지 않으면 false를 반환한다`() {
        val seat =
            Seat(
                rowNumber = RowNumber("A"),
                columnNumber = ColumnNumber(1),
                seatGrade = SeatGrade.GradeB,
            )
        val result = seat.isExist("B2")

        assertThat(result).isFalse()
    }

    @Test
    fun `좌석의 등급이 S이면 가격은 18000원이다`() {
        val seat =
            Seat(
                rowNumber = RowNumber("A"),
                columnNumber = ColumnNumber(1),
                seatGrade = SeatGrade.GradeS,
            )
        assertThat(seat.getPrice().getAmount()).isEqualTo(18000)
    }

    @Test
    fun `좌석의 등급이 A이면 가격은 15000원이다`() {
        val seat =
            Seat(
                rowNumber = RowNumber("A"),
                columnNumber = ColumnNumber(1),
                seatGrade = SeatGrade.GradeA,
            )
        assertThat(seat.getPrice().getAmount()).isEqualTo(15000)
    }

    @Test
    fun `좌석의 등급이 B이면 가격은 13000원이다`() {
        val seat =
            Seat(
                rowNumber = RowNumber("A"),
                columnNumber = ColumnNumber(1),
                seatGrade = SeatGrade.GradeB,
            )
        assertThat(seat.getPrice().getAmount()).isEqualTo(13000)
    }
}
