package movie.domain.movie

import movie.domain.movie.itmes.RunningTime
import movie.domain.movie.itmes.ScreeningPeriod
import movie.domain.movie.itmes.Title
import java.time.LocalDate

class Movie(
    val id: Int? = null,
    val title: Title,
    val runningTime: RunningTime,
    private val screeningPeriod: ScreeningPeriod,
) {
    fun isSameTitle(title: Title): Boolean = this.title == title

    fun isScreening(date: LocalDate): Boolean = this.screeningPeriod.isContain(date)

    fun getTitleText() = title.getTitleText()
}
