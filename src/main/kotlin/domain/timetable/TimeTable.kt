package domain.timetable

import domain.movie.itmes.Title
import domain.timetable.items.ScreeningSchedule
import java.time.LocalDate

class TimeTable(
    private val schedules: List<ScreeningSchedule> = emptyList(),
) {
    fun getMovieSchedulesWithTitle(title: Title): TimeTable {
        val findedSchedules = schedules.filter { it.isScreeningMovieTitle(title) }
        if (findedSchedules.isEmpty()) throw IllegalArgumentException("해당 영화는 사영하고 있지 않습니다.")
        return TimeTable(findedSchedules)
    }

    fun getMovieSchedulesWithDate(date: LocalDate): TimeTable {
        val findedSchedules = schedules.filter { it.isScreeningDate(date) }
        if (findedSchedules.isEmpty()) throw IllegalArgumentException("해당 일자는 상영 계획이 없습니다.")
        return TimeTable(findedSchedules)
    }

    fun countSchedule(): Int = schedules.size

    fun getSchedules(): List<ScreeningSchedule> = schedules

    fun getScheduleWithIndex(index: Int): ScreeningSchedule = schedules[index]
}
