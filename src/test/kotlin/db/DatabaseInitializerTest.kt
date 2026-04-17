package db

import movie.db.DatabaseInitializer
import movie.db.JdbcConnectorFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DatabaseInitializerTest {
    private lateinit var connector: JdbcConnectorFactory
    private lateinit var initializer: DatabaseInitializer

    @BeforeEach
    fun setUp() {
        // 완전한 격리를 위해 매번 새로운 인메모리 DB 사용
        val uniqueName =
            java.util.UUID
                .randomUUID()
                .toString()
        connector = JdbcConnectorFactory("jdbc:h2:mem:$uniqueName;DB_CLOSE_DELAY=-1")
        initializer = DatabaseInitializer(connector)
    }

    @Test
    fun `초기화를 실행하면 모든 필수 테이블이 생성된다`() {
        // 실행
        initializer.initializeTable()

        // 검증
        connector.getConnection().use { conn ->
            val meta = conn.metaData
            val rs = meta.getTables(null, "PUBLIC", null, arrayOf("TABLE"))
            val tables = mutableListOf<String>()
            while (rs.next()) {
                tables.add(rs.getString("TABLE_NAME"))
            }

            assertThat(tables).contains(
                "MOVIE",
                "SCREENING_SCHEDULE",
                "RESERVATION",
                "RESERVED_SEAT",
            )
        }
    }

    @Test
    fun `초기화를 여러 번 실행해도 데이터가 중복으로 삽입되지 않는다`() {
        // 실행 1
        initializer.initializeTable()

        // 실행 2
        initializer.initializeTable()

        // 검증
        connector.getConnection().use { conn ->
            val rs = conn.createStatement().executeQuery("SELECT COUNT(*) FROM MOVIE")
            if (rs.next()) {
                assertThat(rs.getInt(1)).isEqualTo(3) // 초기 데이터 3개 유지 확인
            }
        }
    }
}
