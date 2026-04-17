package movie.service

import movie.domain.movie.itmes.Title
import movie.domain.timetable.TimeTable
import movie.repository.MovieRepository
import movie.repository.ScheduleRepository
import org.springframework.stereotype.Service

@Service
class MovieService(
    private val movieRepository: MovieRepository,
    private val scheduleRepository: ScheduleRepository,
) {
    fun findAllSchedules(): TimeTable {
        return scheduleRepository.findAll()
    }

    fun findSchedulesByTitle(title: String): TimeTable {
        return scheduleRepository.findAllByTitle(Title(title))
    }
}
