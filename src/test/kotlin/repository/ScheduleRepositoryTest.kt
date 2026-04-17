package repository

import movie.db.DatabaseInitializer
import movie.db.JdbcConnectorFactory
import movie.domain.movie.itmes.Title
import movie.repository.ScheduleRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate

class ScheduleRepositoryTest {
    private lateinit var connector: JdbcConnectorFactory
    private lateinit var scheduleRepository: ScheduleRepository

    @BeforeEach
    fun setUp() {
        connector = JdbcConnectorFactory.createTest()
        val initializer = DatabaseInitializer(connector)
        initializer.initializeTable()
        clearDatabaseMemory()
        initializer.initializeTable()
        scheduleRepository = ScheduleRepository(connector)
    }

    @Test
    fun `전체 상영 일정을 확인할 수 있다`() {
        val timeTable = scheduleRepository.findAll()

        assertThat(timeTable.countSchedule()).isEqualTo(15)
    }

    @Test
    fun `제목을 통해 해당 영화의 상영 일정을 갖는 TimeTable객체를 반환한다`() {
        val timeTable = scheduleRepository.findAllByTitle(Title("살묵지"))

        assertThat(timeTable.countSchedule()).isEqualTo(5)
    }

    @Test
    fun `입력받은 제목을 갖는 상영 일정이 없다면 빈 TimeTable객체를 반환한다`() {
        val timeTable = scheduleRepository.findAllByTitle(Title("없는 영화"))

        assertThat(timeTable.isEmpty()).isTrue()
    }

    @Test
    fun `날짜를 통해 해당 일자의 상열 일정을 갖는 TimeTable 객체를 반환한다`() {
        val timeTable = scheduleRepository.findAllByDate(LocalDate.of(2026, 4, 3))

        assertThat(timeTable.countSchedule()).isEqualTo(15)
    }

    @Test
    fun `입력받은 날짜에 사영하는 영화가 없으면 빈 TimeTable객체를 반환한다`() {
        val timeTable = scheduleRepository.findAllByDate(LocalDate.of(9999, 4, 3))

        assertThat(timeTable.isEmpty()).isTrue()
    }

    private fun clearDatabaseMemory() {
        connector.getConnection().use {
            val statement = it.createStatement()
            statement.execute("SET REFERENTIAL_INTEGRITY FALSE")
            statement.execute("TRUNCATE TABLE RESERVED_SEAT RESTART IDENTITY ")
            statement.execute("TRUNCATE TABLE RESERVATION RESTART IDENTITY ")
            statement.execute("TRUNCATE TABLE SCREENING_SCHEDULE RESTART IDENTITY ")
            statement.execute("TRUNCATE TABLE MOVIE RESTART IDENTITY ")
            statement.execute("SET REFERENTIAL_INTEGRITY TRUE")
        }
    }
}
