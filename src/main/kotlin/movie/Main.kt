package movie

import movie.controller.Controller
import movie.db.DatabaseInitializer
import movie.db.JdbcConnectorFactory
import movie.repository.MovieRepository
import movie.repository.ReservationRepository
import movie.repository.ScheduleRepository

fun main() {
    val connector = JdbcConnectorFactory.Companion.createLocal()

    val databaseInitializer = DatabaseInitializer(connector)
    databaseInitializer.initializeTable()

    val movieRepository = MovieRepository(connector)
    val scheduleRepository = ScheduleRepository(connector)
    val reservationRepository = ReservationRepository(connector)

    val controller =
        Controller(
            movieRepository = movieRepository,
            scheduleRepository = scheduleRepository,
            reservationRepository = reservationRepository,
        )
    controller.run()
}
