package movie

import movie.db.JdbcConnectorFactory
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import java.time.Duration

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = ["spring.datasource.url=jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"],
)
class ApplicationTest(
    @LocalServerPort private val port: Int,
) {
    @Autowired
    private lateinit var connector: JdbcConnectorFactory
    private lateinit var client: WebTestClient

    @BeforeEach
    fun setUp() {
        client =
            WebTestClient
                .bindToServer()
                .baseUrl("http://localhost:$port")
                .responseTimeout(Duration.ofSeconds(10))
                .build()

        clearDatabase()
    }

    private fun clearDatabase() {
        connector.getConnection().use { conn ->
            val statement = conn.createStatement()
            statement.execute("SET REFERENTIAL_INTEGRITY FALSE")
            statement.execute("TRUNCATE TABLE RESERVED_SEAT RESTART IDENTITY")
            statement.execute("TRUNCATE TABLE RESERVATION RESTART IDENTITY")
            statement.execute("TRUNCATE TABLE SCREENING_SCHEDULE RESTART IDENTITY")
            statement.execute("TRUNCATE TABLE MOVIE RESTART IDENTITY")
            statement.execute("SET REFERENTIAL_INTEGRITY TRUE")

            insertTestData(conn)
        }
    }

    private fun insertTestData(conn: java.sql.Connection) {
        val statement = conn.createStatement()
        // ID를 명시적으로 지정하여 테스트에서 예측 가능하게 합니다.
        statement.execute(
            "INSERT INTO MOVIE (id, title, running_time, start_date, end_date) VALUES (1, '테스트 영화', 120, '2026-04-01', '2026-04-30')",
        )
        statement.execute(
            "INSERT INTO SCREENING_SCHEDULE (id, movie_id, start_time, end_time, screening_date) VALUES (1, 1, '10:00', '12:00', '2026-04-03')",
        )
        statement.execute(
            "INSERT INTO SCREENING_SCHEDULE (id, movie_id, start_time, end_time, screening_date) VALUES (2, 1, '13:00', '15:00', '2026-04-03')",
        )
        statement.execute(
            "INSERT INTO SCREENING_SCHEDULE (id, movie_id, start_time, end_time, screening_date) VALUES (3, 1, '16:00', '18:00', '2026-04-03')",
        )
    }

    @Test
    fun `영화 목록 조회 요청 시 200 OK를 반환한다`() {
        client
            .get()
            .uri("/api/movies")
            .exchange()
            .expectStatus()
            .isOk
    }

    @Test
    fun `영화 목록 조회 응답에 영화와 상영 정보가 올바르게 포함된다`() {
        client
            .get()
            .uri("/api/movies")
            .exchange()
            .expectBody()
            .jsonPath("$.movies")
            .isArray
            .jsonPath("$.movies[0].id")
            .exists()
            .jsonPath("$.movies[0].title")
            .isEqualTo("테스트 영화")
            .jsonPath("$.movies[0].runningTimeMinutes")
            .isEqualTo(120)
            .jsonPath("$.movies[0].screenings")
            .isArray
    }

    @Test
    fun `예매 생성 요청 시 201 Created를 반환한다`() {
        val requestBody =
            """
            {
              "reservations": [
                {
                  "screeningId": 1,
                  "seats": ["A1", "A2"]
                }
              ],
              "usedPoints": 0,
              "paymentMethod": "CREDIT_CARD"
            }
            """.trimIndent()

        client
            .post()
            .uri("/api/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(requestBody)
            .exchange()
            .expectStatus()
            .isCreated
    }

    @Test
    fun `예매 생성 응답에 예매 정보와 결제 금액이 올바르게 포함된다`() {
        val requestBody =
            """
            {
              "reservations": [
                {
                  "screeningId": 2,
                  "seats": ["B1"]
                }
              ],
              "usedPoints": 0,
              "paymentMethod": "CASH"
            }
            """.trimIndent()

        client
            .post()
            .uri("/api/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(requestBody)
            .exchange()
            .expectStatus()
            .isCreated
            .expectBody()
            .jsonPath("$.reservationId")
            .exists()
            .jsonPath("$.reservations")
            .isArray
            .jsonPath("$.totalPrice")
            .exists()
    }

    @Test
    fun `이미 예약된 좌석을 예매하려는 경우 400 BadRequest 코드를 반환한다`() {
        val requestBody =
            """
            {
              "reservations": [
                {
                  "screeningId": 3,
                  "seats": ["C1"]
                }
              ],
              "usedPoints": 0,
              "paymentMethod": "CREDIT_CARD"
            }
            """.trimIndent()

        // 첫 번째 시도 (성공)
        client
            .post()
            .uri("/api/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(requestBody)
            .exchange()
            .expectStatus()
            .isCreated

        // 두 번째 시도 (실패 - 400 Bad Request)
        client
            .post()
            .uri("/api/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(requestBody)
            .exchange()
            .expectStatus()
            .isBadRequest
    }

    @Test
    fun `존재하지 않는 상영에 대해 예매를 요청하면 400 BadRequest 코드를 반환한다`() {
        val requestBody =
            """
            {
              "reservations": [
                {
                  "screeningId": 9999,
                  "seats": ["A1"]
                }
              ],
              "usedPoints": 0,
              "paymentMethod": "CREDIT_CARD"
            }
            """.trimIndent()

        client
            .post()
            .uri("/api/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(requestBody)
            .exchange()
            .expectStatus()
            .isBadRequest
    }

    @Test
    fun `잘못된 요청 형식에 대해 400 BadRequest 코드를 반환한다`() {
        val badRequestBody = " { \"invalid\": " // 형식이 깨진 JSON

        client
            .post()
            .uri("/api/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(badRequestBody)
            .exchange()
            .expectStatus()
            .isBadRequest
    }
}
