package movie.repository

import movie.db.JdbcConnectorFactory
import movie.domain.movie.Movie
import movie.domain.movie.itmes.RunningTime
import movie.domain.movie.itmes.ScreeningPeriod
import movie.domain.movie.itmes.Title
import movie.domain.timetable.TimeTable
import movie.domain.timetable.items.ScreenTime
import movie.domain.timetable.items.ScreeningSchedule
import org.springframework.stereotype.Repository
import java.sql.Date
import java.sql.ResultSet
import java.time.LocalDate

@Repository
class ScheduleRepository(
    private val connector: JdbcConnectorFactory,
) {
    fun findAll(): TimeTable {
        val sql =
            """
            SELECT *
            FROM SCREENING_SCHEDULE s JOIN MOVIE m ON s.movie_id = m.id
            """.trimIndent()
        val schedules = mutableListOf<ScreeningSchedule>()
        connector.getConnection().use {
            val statement = it.createStatement()
            val resultSet = statement.executeQuery(sql)
            while (resultSet.next()) {
                schedules.add(mapToScreeningSchedule(resultSet))
            }
        }
        return TimeTable(schedules)
    }

    fun findAllByTitle(title: Title): TimeTable {
        val sql =
            """
            SELECT * 
            FROM SCREENING_SCHEDULE s JOIN MOVIE m ON s.movie_id = m.id
            WHERE m.title = ?
            """.trimIndent()
        val schedules = mutableListOf<ScreeningSchedule>()
        connector.getConnection().use {
            val statement = it.prepareStatement(sql)
            statement.setString(1, title.title)
            val resultSet = statement.executeQuery()
            while (resultSet.next()) {
                val schedule = mapToScreeningSchedule(resultSet)
                requireNotNull(schedule) { "해당 영화의 상영 일정이 존재하지 않습니다." }
                schedules.add(schedule)
            }
        }
        return TimeTable(schedules)
    }

    fun findAllByDate(date: LocalDate): TimeTable {
        val sql =
            """
            SELECT * 
            FROM SCREENING_SCHEDULE s JOIN MOVIE m ON s.movie_id = m.id
            WHERE s.screening_date = ?
            """.trimIndent()
        val schedules = mutableListOf<ScreeningSchedule>()
        connector.getConnection().use {
            val statement = it.prepareStatement(sql)
            statement.setDate(1, Date.valueOf(date))
            val resultSet = statement.executeQuery()
            while (resultSet.next()) {
                val schedule = mapToScreeningSchedule(resultSet)
                schedules.add(schedule)
            }
        }
        return TimeTable(schedules)
    }

    fun findById(id: Int): ScreeningSchedule? {
        val sql =
            """
            SELECT * 
            FROM SCREENING_SCHEDULE s JOIN MOVIE m ON s.movie_id = m.id
            WHERE s.id = ?
            """.trimIndent()
        connector.getConnection().use {
            val statement = it.prepareStatement(sql)
            statement.setInt(1, id)
            val resultSet = statement.executeQuery()
            return if (resultSet.next()) mapToScreeningSchedule(resultSet) else null
        }
    }

    private fun mapToScreeningSchedule(resultSet: ResultSet): ScreeningSchedule {
        val movie =
            Movie(
                id = resultSet.getInt("movie_id"),
                title = Title(resultSet.getString("title")),
                runningTime = RunningTime(resultSet.getInt("running_time")),
                screeningPeriod =
                    ScreeningPeriod(
                        startDate = resultSet.getDate("start_date").toLocalDate(),
                        endDate = resultSet.getDate("end_date").toLocalDate(),
                    ),
            )

        val screenTime =
            ScreenTime(
                startTime = resultSet.getTime("start_time").toLocalTime(),
                endTime = resultSet.getTime("end_time").toLocalTime(),
                screeningDate = resultSet.getDate("screening_date").toLocalDate(),
            )
        return ScreeningSchedule(resultSet.getInt("id"), movie, screenTime)
    }
}
