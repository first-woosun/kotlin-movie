package movie.repository

import movie.db.JdbcConnectorFactory
import movie.domain.money.Money
import movie.domain.movie.Movie
import movie.domain.movie.itmes.RunningTime
import movie.domain.movie.itmes.ScreeningPeriod
import movie.domain.movie.itmes.Title
import movie.domain.reservations.items.Reservation
import movie.domain.seat.Seat
import movie.domain.seat.items.ColumnNumber
import movie.domain.seat.items.RowNumber
import movie.domain.timetable.items.ScreenTime
import org.springframework.stereotype.Repository
import java.sql.Connection
import java.sql.Date
import java.sql.Statement
import java.time.LocalDate

@Repository
class ReservationRepository(
    private val connector: JdbcConnectorFactory,
) {
    fun save(
        scheduleId: Int,
        reservation: Reservation,
        totalPrice: Money,
    ) {
        val reservationInfo = reservation.getReservationInfo()
        val seats = reservationInfo.seats

        connector.getConnection().use { conn ->
            conn.autoCommit = false
            try {
                val reservationId = insertReservation(conn, scheduleId, totalPrice.amount)
                insertReservedSeats(conn, reservationId, seats)
                conn.commit()
            } catch (e: Exception) {
                conn.rollback()
                throw e
            }
        }
    }

    fun findReservedSeatsByScheduleId(scheduleId: Int): List<Seat> {
        val sql =
            """
            SELECT rs.row_number, rs.column_number
            FROM RESERVED_SEAT rs JOIN RESERVATION r on rs.reservation_id = r.id
            WHERE r.schedule_id = ?
            """.trimIndent()
        val reservedSeats = mutableListOf<Seat>()
        connector.getConnection().use { conn ->
            val statement = conn.prepareStatement(sql)
            statement.setInt(1, scheduleId)
            val resultSet = statement.executeQuery()
            while (resultSet.next()) {
                reservedSeats.add(
                    Seat.Companion.create(
                        RowNumber(resultSet.getString("row_number")),
                        ColumnNumber(resultSet.getInt("column_number")),
                    ),
                )
            }
        }
        return reservedSeats
    }

    private fun insertReservation(
        conn: Connection,
        scheduleId: Int,
        price: Int,
    ): Int {
        val sql = "INSERT INTO RESERVATION (schedule_id, total_price) VALUES (?, ?)"
        val statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        statement.setInt(1, scheduleId)
        statement.setInt(2, price)
        statement.executeUpdate()

        val resultKey = statement.generatedKeys
        return if (resultKey.next()) resultKey.getInt(1) else throw IllegalArgumentException("예매 ID 생성 실패")
    }

    private fun insertReservedSeats(
        conn: Connection,
        reservationId: Int,
        seats: List<Seat>,
    ) {
        val sql = "INSERT INTO RESERVED_SEAT (reservation_id, row_number, column_number) VALUES (?, ?, ?)"
        val statement = conn.prepareStatement(sql)

        for (seat in seats) {
            statement.setInt(1, reservationId)
            statement.setString(2, seat.rowNumber.rowNumber)
            statement.setInt(3, seat.columnNumber.columnNumber)
            statement.executeUpdate()
        }
    }

    fun findAllByDate(date: LocalDate): List<Reservation> {
        val sql =
            """
            SELECT r.id, r.total_price, s.id as schedule_id, s.start_time, s.end_time, s.screening_date,
                   m.id as movie_id, m.title, m.running_time, m.start_date as movie_start, m.end_date as movie_end
            FROM RESERVATION r
            JOIN SCREENING_SCHEDULE s ON r.schedule_id = s.id
            JOIN MOVIE m ON s.movie_id = m.id
            WHERE s.screening_date = ?
            """.trimIndent()

        val reservations = mutableListOf<Reservation>()
        connector.getConnection().use { conn ->
            val pstmt = conn.prepareStatement(sql)
            pstmt.setDate(1, Date.valueOf(date))
            val rs = pstmt.executeQuery()

            while (rs.next()) {
                val resId = rs.getInt("id")
                val seats = findSeatsByReservationId(conn, resId)

                val movie =
                    Movie(
                        rs.getInt("movie_id"),
                        Title(rs.getString("title")),
                        RunningTime(rs.getInt("running_time")),
                        ScreeningPeriod(rs.getDate("movie_start").toLocalDate(), rs.getDate("movie_end").toLocalDate()),
                    )
                val screenTime =
                    ScreenTime(
                        rs.getTime("start_time").toLocalTime(),
                        rs.getTime("end_time").toLocalTime(),
                        rs.getDate("screening_date").toLocalDate(),
                    )
                reservations.add(Reservation(id = resId, movie = movie, screenTime = screenTime, seats = seats))
            }
        }
        return reservations
    }

    private fun findSeatsByReservationId(
        conn: Connection,
        reservationId: Int,
    ): List<Seat> {
        val sql = "SELECT row_number, column_number FROM RESERVED_SEAT WHERE reservation_id = ?"
        val seats = mutableListOf<Seat>()
        val pstmt = conn.prepareStatement(sql)
        pstmt.setInt(1, reservationId)
        val rs = pstmt.executeQuery()
        while (rs.next()) {
            seats.add(
                Seat.Companion.create(
                    RowNumber(rs.getString("row_number")),
                    ColumnNumber(rs.getInt("column_number")),
                ),
            )
        }
        return seats
    }
}
