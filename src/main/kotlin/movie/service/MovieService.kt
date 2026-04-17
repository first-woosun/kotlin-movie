package movie.service

import movie.controller.dto.MovieDetailResponse
import movie.controller.dto.MoviesResponse
import movie.controller.dto.ScreeningResponse
import movie.repository.ScheduleRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class MovieService(
    private val scheduleRepository: ScheduleRepository,
) {
    fun findAllMoviesWithScreenings(): MoviesResponse {
        val allSchedules = scheduleRepository.findAll().getSchedules()

        val schedulesByMovie = allSchedules.groupBy { it.getMovie() }

        val movieDetailResponses =
            schedulesByMovie.map { (movie, schedules) ->
                MovieDetailResponse(
                    id = movie.id ?: 0,
                    title = movie.title.title,
                    runningTimeMinutes = movie.runningTime.runningTime,
                    screenings =
                        schedules.map { schedule ->
                            ScreeningResponse(
                                id = schedule.id ?: 0,
                                startAt =
                                    LocalDateTime.of(
                                        schedule.getScreenTime().screeningDate,
                                        schedule.getScreenTime().startTime,
                                    ),
                                endAt =
                                    LocalDateTime.of(
                                        schedule.getScreenTime().screeningDate,
                                        schedule.getScreenTime().endTime,
                                    ),
                            )
                        },
                )
            }

        return MoviesResponse(movies = movieDetailResponses)
    }
}
