package db

import java.sql.Connection
import java.sql.DriverManager

class JdbcConnectorFactory(
    private val url: String,
    private val user: String = "sa",
    private val password: String = ""
) {
    fun getConnection(): Connection = DriverManager.getConnection(url, user, password)

    companion object {
        fun createLocal() = JdbcConnectorFactory("jdbc:h2:~/Kotlin-Movie")

        fun createTest() = JdbcConnectorFactory("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1")
    }
}