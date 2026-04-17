import controller.Controller
import db.DatabaseInitializer
import db.JdbcConnectorFactory
import repository.MovieRepository
import repository.ReservationRepository
import repository.ScheduleRepository

fun main() {
    val connector = JdbcConnectorFactory.createLocal()

    val databaseInitializer = DatabaseInitializer(connector)
    databaseInitializer.initializeTable()

    val movieRepository = MovieRepository(connector)
    val scheduleRepository = ScheduleRepository(connector)
    val reservationRepository = ReservationRepository(connector)

    val controller = Controller(
        movieRepository = movieRepository,
        scheduleRepository = scheduleRepository,
        reservationRepository = reservationRepository
    )
    controller.run()
}
