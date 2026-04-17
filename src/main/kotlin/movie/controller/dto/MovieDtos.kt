package movie.controller.dto

import java.time.LocalDateTime

/**
 * GET /api/movies 응답을 위한 최상위 DTO
 */
data class MoviesResponse(
    val movies: List<MovieDetailResponse>,
)

/**
 * 개별 영화 정보를 담는 DTO
 */
data class MovieDetailResponse(
    val id: Int,
    val title: String,
    val runningTimeMinutes: Int,
    val screenings: List<ScreeningResponse>,
)

/**
 * 상영 일정 정보를 담는 DTO
 */
data class ScreeningResponse(
    val id: Int,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime,
)
