package movie.controller

import movie.controller.dto.MoviesResponse
import movie.service.MovieService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/movies")
class MovieController(
    private val movieService: MovieService,
) {
    @GetMapping
    fun getMovies(): ResponseEntity<MoviesResponse> {
        val response = movieService.findAllMoviesWithScreenings()
        return response
            .takeIf { it.movies.isNotEmpty() }
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.noContent().build()
    }
}
