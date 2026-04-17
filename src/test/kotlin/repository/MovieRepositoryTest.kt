package repository

import movie.db.DatabaseInitializer
import movie.db.JdbcConnectorFactory
import movie.domain.movie.itmes.Title
import movie.repository.MovieRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MovieRepositoryTest {
    private lateinit var movieRepository: MovieRepository

    @BeforeEach
    fun setUp() {
        val connector = JdbcConnectorFactory.createTest()
        val initializer = DatabaseInitializer(connector)
        initializer.initializeTable()
        movieRepository = MovieRepository(connector)
    }

    @Test
    fun `데이터베이스에 저장된 전체 영화 목록을 조회할 수 있다`() {
        val movies = movieRepository.findAll()

        assertThat(movies.map { it.getTitleText() }).containsExactlyInAnyOrder("신바드의 모험", "아이언맨", "살묵지")
    }

    @Test
    fun `입력된 제목의 영화가 테이블에 있다면 해당 영화의 Movie객체를 반환한다`() {
        val movie = movieRepository.findByTitle(Title("아이언맨"))

        assertThat(movie).isNotNull()
        assertThat(movie?.getTitleText()).isEqualTo("아이언맨")
    }

    @Test
    fun `입력받은 제목과 동일한 제목을 갖는 영화가 없다면 null을 반환한다`() {
        val movie = movieRepository.findByTitle(Title("없는영화"))
        assertThat(movie).isNull()
    }

    @Test
    fun `ID를 통해 특정 영화를 조회할 수 있다`() {
        val movie = movieRepository.findById(2)
        assertThat(movie?.getTitleText()).isEqualTo("아이언맨")
    }
}
