package repository

import movie.db.DatabaseInitializer
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
import movie.repository.ReservationRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.sql.Statement
import java.time.LocalDate
import java.time.LocalTime

class ReservationRepositoryTest {
    private lateinit var connector: JdbcConnectorFactory
    private lateinit var reservationRepository: ReservationRepository
    private var testScheduleId = -1

    @BeforeEach
    fun setUp() {
        connector = JdbcConnectorFactory.createTest()
        val initializer = DatabaseInitializer(connector)
        initializer.initializeTable()
        clearDatabaseMemory()

        // 중요: 테스트 기초 데이터를 준비하고 반환된 ID를 저장합니다.
        testScheduleId = prepareTestData()

        reservationRepository = ReservationRepository(connector)
    }

    @Test
    fun `예매 정보와 좌석 목록을 저장하면 DB에 정상적으로 기록된다`() {
        val seats =
            listOf(
                Seat.create(RowNumber("A"), ColumnNumber(1)),
                Seat.create(RowNumber("A"), ColumnNumber(2)),
            )
        val reservation = createReservation(seats)
        val totalPrice = Money(30000)

        reservationRepository.save(testScheduleId, reservation, totalPrice)

        val reservedSeats = reservationRepository.findReservedSeatsByScheduleId(testScheduleId)
        assertThat(reservedSeats.map { it.getSeatNumber() }).containsExactly("A1", "A2")
    }

    @Test
    fun `특정 상영 일정에 예약된 모든 좌석 목록을 조회할 수 있다`() {
        val totalPrice = Money(15000)

        val reservation1 =
            createReservation(
                listOf(
                    Seat.create(RowNumber("A"), ColumnNumber(1)),
                    Seat.create(RowNumber("A"), ColumnNumber(2)),
                ),
            )
        reservationRepository.save(testScheduleId, reservation1, totalPrice)

        val reservation2 =
            createReservation(
                listOf(
                    Seat.create(RowNumber("B"), ColumnNumber(1)),
                ),
            )
        reservationRepository.save(testScheduleId, reservation2, totalPrice)

        val reservedSeats = reservationRepository.findReservedSeatsByScheduleId(testScheduleId)

        // 검증
        assertThat(reservedSeats).hasSize(3)
        assertThat(reservedSeats.map { it.getSeatNumber() })
            .containsExactlyInAnyOrder("A1", "A2", "B1")
    }

    @Test
    fun `예약된 좌석이 없는 상영 일정을 조회하면 빈 리스트가 반환된다`() {
        val emptyScheduleId = prepareTestData()

        val reservedSeats = reservationRepository.findReservedSeatsByScheduleId(emptyScheduleId)

        assertThat(reservedSeats).isEmpty()
    }

    @Test
    fun `이미 예매된 시간과 겹치는 새로운 예매가 있는지 확인할 수 있다`() {
        val existingSeats = listOf(Seat.create(RowNumber("A"), ColumnNumber(1)))
        val existingReservation = createReservation(existingSeats)
        reservationRepository.save(testScheduleId, existingReservation, Money(15000))

        val savedReservations = reservationRepository.findAllByDate(LocalDate.of(2026, 5, 1))

        val newReservationTime = LocalTime.of(11, 0)
        val isOverlapped = savedReservations.any { it.isDuplicatedTime(newReservationTime) }

        assertThat(isOverlapped).isTrue()
    }

    @Test
    fun `시간이 겹치지 않는 예매는 중복으로 판단되지 않는다`() {
        val existingReservation = createReservation(listOf(Seat.create(RowNumber("A"), ColumnNumber(1))))
        reservationRepository.save(testScheduleId, existingReservation, Money(15000))

        val savedReservations = reservationRepository.findAllByDate(LocalDate.of(2026, 5, 1))

        val newReservationTime = LocalTime.of(14, 0)
        val isOverlapped = savedReservations.any { it.isDuplicatedTime(newReservationTime) }

        assertThat(isOverlapped).isFalse()
    }

    private fun prepareTestData(): Int {
        connector.getConnection().use { conn ->
            val movieStatement =
                conn.prepareStatement(
                    "INSERT INTO MOVIE (title, running_time, start_date, end_date) VALUES ('테스트', 120, '2026-04-10', '2026-04-30')",
                    Statement.RETURN_GENERATED_KEYS,
                )
            movieStatement.executeUpdate()
            val movieRs = movieStatement.generatedKeys
            val movieId = if (movieRs.next()) movieRs.getInt(1) else -1

            val scheduleStatement =
                conn.prepareStatement(
                    "INSERT INTO SCREENING_SCHEDULE (movie_id, start_time, end_time, screening_date) VALUES (?, '10:00', '12:00', '2026-05-01')",
                    Statement.RETURN_GENERATED_KEYS,
                )
            scheduleStatement.setInt(1, movieId)
            scheduleStatement.executeUpdate()

            val scheduleRs = scheduleStatement.generatedKeys
            return if (scheduleRs.next()) scheduleRs.getInt(1) else -1
        }
    }

    private fun clearDatabaseMemory() {
        connector.getConnection().use {
            val statement = it.createStatement()
            statement.execute("SET REFERENTIAL_INTEGRITY FALSE")
            statement.execute("TRUNCATE TABLE RESERVED_SEAT RESTART IDENTITY")
            statement.execute("TRUNCATE TABLE RESERVATION RESTART IDENTITY")
            statement.execute("TRUNCATE TABLE SCREENING_SCHEDULE RESTART IDENTITY")
            statement.execute("TRUNCATE TABLE MOVIE RESTART IDENTITY")
            statement.execute("SET REFERENTIAL_INTEGRITY TRUE")
        }
    }

    private fun createReservation(seats: List<Seat>): Reservation {
        val movie = Movie(null, Title("테스트"), RunningTime(120), ScreeningPeriod(LocalDate.of(2026, 4, 10), LocalDate.of(2026, 4, 30)))
        val screenTime = ScreenTime(LocalTime.of(10, 0), LocalTime.of(12, 0), LocalDate.of(2026, 5, 1))
        return Reservation(
            movie = movie,
            screenTime = screenTime,
            seats = seats,
        )
    }
}
