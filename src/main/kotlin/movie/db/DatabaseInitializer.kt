package movie.db

import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component
import java.sql.Connection

@Component
class DatabaseInitializer(
    private val connector: JdbcConnectorFactory,
) {
    @PostConstruct
    fun initializeTable() {
        connector.getConnection().use {
            createTables(it)
            insertData(it)
        }
    }

    private fun createTables(connection: Connection) {
        val statement = connection.createStatement()

        statement.execute(
            """
            CREATE TABLE IF NOT EXISTS MOVIE (
                id INT AUTO_INCREMENT PRIMARY KEY,
                title VARCHAR(255) NOT NULL,
                running_time INT NOT NULL,
                start_date DATE NOT NULL,
                end_date DATE NOT NULL
            )
            """.trimIndent(),
        )

        statement.execute(
            """
            CREATE TABLE IF NOT EXISTS SCREENING_SCHEDULE (
                id INT AUTO_INCREMENT PRIMARY KEY,
                movie_id INT NOT NULL,
                start_time TIME NOT NULL,
                end_time TIME NOT NULL,
                screening_date DATE NOT NULL,
                FOREIGN KEY (movie_id) REFERENCES MOVIE(id)
            )
            """.trimIndent(),
        )

        statement.execute(
            """
            CREATE TABLE IF NOT EXISTS RESERVATION (
                id INT AUTO_INCREMENT PRIMARY KEY,
                schedule_id INT NOT NULL,
                total_price INT NOT NULL,
                FOREIGN KEY (schedule_id) REFERENCES SCREENING_SCHEDULE(ID)
            )
            """.trimIndent(),
        )

        statement.execute(
            """
            CREATE TABLE IF NOT EXISTS RESERVED_SEAT (
                id INT AUTO_INCREMENT PRIMARY KEY,
                reservation_id INT NOT NULL,
                row_number VARCHAR(10) NOT NULL,
                column_number INT NOT NULL,
                FOREIGN KEY (reservation_id) REFERENCES RESERVATION(id)
            )
            """.trimIndent(),
        )
    }

    fun insertData(connection: Connection) {
        val statement = connection.createStatement()

        val resultSet = statement.executeQuery("SELECT COUNT(*) FROM MOVIE")
        if (resultSet.next() && resultSet.getInt(1) == 0) {
            statement.execute(
                """
                INSERT INTO MOVIE (title, running_time, start_date, end_date)
                VALUES 
                    ('신바드의 모험', 120, '2026-04-01', '2026-04-30'),
                    ('아이언맨', 180, '2026-04-10', '2026-05-10'),
                    ('살묵지', 160, '2026-04-12', '2026-05-17')
                """.trimIndent(),
            )
            statement.execute(
                """
                INSERT INTO SCREENING_SCHEDULE (movie_id, start_time, end_time, screening_date)
                VALUES 
                    (1, '10:00', '12:00', '2026-04-03'),
                    (1, '12:30', '14:30', '2026-04-03'),
                    (1, '15:00', '17:00', '2026-04-03'),
                    (1, '17:30', '19:30', '2026-04-03'),
                    (1, '20:00', '22:00', '2026-04-03'),
                    (2, '10:00', '12:00', '2026-04-03'),
                    (2, '12:30', '14:30', '2026-04-03'),
                    (2, '15:00', '17:00', '2026-04-03'),
                    (2, '17:30', '19:30', '2026-04-03'),
                    (2, '20:00', '22:00', '2026-04-03'),
                    (3, '10:00', '12:00', '2026-04-03'),
                    (3, '12:30', '14:30', '2026-04-03'),
                    (3, '15:00', '17:00', '2026-04-03'),
                    (3, '17:30', '19:30', '2026-04-03'),
                    (3, '20:00', '22:00', '2026-04-03')
                """.trimIndent(),
            )
        }
    }
}
