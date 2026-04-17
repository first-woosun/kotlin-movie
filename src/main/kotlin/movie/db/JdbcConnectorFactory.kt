package movie.db

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.sql.Connection
import java.sql.DriverManager

@Component
class JdbcConnectorFactory(
    @Value("\${spring.datasource.url:jdbc:h2:~/Kotlin-Movie}")
    private val url: String,
    @Value("\${spring.datasource.username:sa}")
    private val user: String = "sa",
    @Value("\${spring.datasource.password:}")
    private val password: String = "",
) {
    fun getConnection(): Connection = DriverManager.getConnection(url, user, password)

    companion object {
        fun createLocal() = JdbcConnectorFactory("jdbc:h2:~/Kotlin-Movie")

        fun createTest() = JdbcConnectorFactory("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1")
    }
}
