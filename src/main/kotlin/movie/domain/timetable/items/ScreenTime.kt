package movie.domain.timetable.items

import java.time.LocalDate
import java.time.LocalTime

class ScreenTime(
    val startTime: LocalTime,
    val endTime: LocalTime,
    val screeningDate: LocalDate,
) {
    fun isContain(time: LocalTime): Boolean = time in startTime..endTime

    fun isStartAt(time: LocalTime): Boolean = startTime == time

    fun isStartBefore(time: LocalTime): Boolean = startTime <= time

    fun isStartAfter(time: LocalTime): Boolean = startTime >= time

    fun isScreeningAt(date: LocalDate): Boolean = screeningDate == date

    fun isSameDate(date: Int): Boolean = screeningDate.dayOfMonth == date

    fun startTimeToString() = startTime.toString()
}
