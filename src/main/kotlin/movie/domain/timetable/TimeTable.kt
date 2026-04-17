package movie.domain.timetable

import movie.domain.movie.itmes.Title
import movie.domain.timetable.items.ScreeningSchedule
import java.time.LocalDate

class TimeTable(
    private val schedules: List<ScreeningSchedule> = emptyList(),
) {
    fun getMovieSchedulesWithTitle(title: Title): TimeTable {
        val findedSchedules = schedules.filter { it.isScreeningMovieTitle(title) }
        if (findedSchedules.isEmpty()) return TimeTable(emptyList())
        return TimeTable(findedSchedules)
    }

    fun getMovieSchedulesWithDate(date: LocalDate): TimeTable {
        val findedSchedules = schedules.filter { it.isScreeningDate(date) }
        if (findedSchedules.isEmpty()) return TimeTable(emptyList())
        return TimeTable(findedSchedules)
    }

    fun countSchedule(): Int = schedules.size

    fun getSchedules(): List<ScreeningSchedule> = schedules

    fun getScheduleWithIndex(index: Int): ScreeningSchedule = schedules[index]

    fun isEmpty(): Boolean = schedules.isEmpty()
}
