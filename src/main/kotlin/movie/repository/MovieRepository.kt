package movie.repository

import movie.db.JdbcConnectorFactory
import movie.domain.movie.Movie
import movie.domain.movie.itmes.RunningTime
import movie.domain.movie.itmes.ScreeningPeriod
import movie.domain.movie.itmes.Title
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class MovieRepository(
    private val connector: JdbcConnectorFactory,
) {
    fun findAll(): List<Movie> {
        val movies = mutableListOf<Movie>()
        val sql = "SELECT * FROM MOVIE"
        connector.getConnection().use {
            val statement = it.createStatement()
            val resultSet = statement.executeQuery(sql)
            while (resultSet.next()) {
                movies.add(mapToMovie(resultSet))
            }
        }
        return movies
    }

    fun findByTitle(title: Title): Movie? {
        val sql = "SELECT * FROM MOVIE WHERE title = ?"
        connector.getConnection().use {
            val statement = it.prepareStatement(sql)
            statement.setString(1, title.title)
            val resultSet = statement.executeQuery()
            return if (resultSet.next()) mapToMovie(resultSet) else null
        }
    }

    fun findById(id: Int): Movie? {
        val sql = "SELECT * FROM MOVIE WHERE id = ?"
        connector.getConnection().use {
            val statement = it.prepareStatement(sql)
            statement.setInt(1, id)
            val resultSet = statement.executeQuery()
            return if (resultSet.next()) mapToMovie(resultSet) else null
        }
    }

    private fun mapToMovie(resultSet: ResultSet): Movie =
        Movie(
            title = Title(resultSet.getString("title")),
            runningTime = RunningTime(resultSet.getInt("running_time")),
            screeningPeriod =
                ScreeningPeriod(
                    startDate = resultSet.getDate("start_date").toLocalDate(),
                    endDate = resultSet.getDate("end_date").toLocalDate(),
                ),
        )
}
