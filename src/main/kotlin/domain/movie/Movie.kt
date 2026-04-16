package domain.movie

import domain.movie.itmes.RunningTime
import domain.movie.itmes.ScreeningPeriod
import domain.movie.itmes.Title
import java.time.LocalDate

class Movie(
    private val id: Int? = null,
    private val title: Title,
    private val runningTime: RunningTime,
    private val screeningPeriod: ScreeningPeriod,
) {
    fun isSameTitle(title: Title): Boolean = this.title == title

    fun isScreening(date: LocalDate): Boolean = this.screeningPeriod.isContain(date)

    fun getTitleText() = title.getTitleText()
}
