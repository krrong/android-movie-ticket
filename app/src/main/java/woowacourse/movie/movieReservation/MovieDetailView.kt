package woowacourse.movie.movieReservation

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import woowacourse.movie.R
import woowacourse.movie.uimodel.MovieModelUi
import woowacourse.movie.utils.DateUtil

class MovieDetailView(
    private val view: View,
) {

    private val moviePosterView: ImageView
    private val movieTitleView: TextView
    private val movieReleaseDataView: TextView
    private val movieRunningTimeView: TextView
    private val movieSummaryView: TextView

    init {
        moviePosterView = view.findViewById(R.id.reservation_movie_poster)
        movieTitleView = view.findViewById(R.id.reservation_movie_title)
        movieReleaseDataView = view.findViewById(R.id.reservation_movie_release_date)
        movieRunningTimeView = view.findViewById(R.id.reservation_movie_running_time)
        movieSummaryView = view.findViewById(R.id.reservation_movie_summary)
    }

    fun initMovieView(movieScheduleUi: MovieModelUi.MovieScheduleUi) {
        moviePosterView.setImageResource(movieScheduleUi.poster)
        movieTitleView.text = movieScheduleUi.title
        movieReleaseDataView.text = DateUtil(view.context).getDateRange(movieScheduleUi.startDate, movieScheduleUi.endDate)
        movieRunningTimeView.text = view.context.getString(R.string.movie_running_time, movieScheduleUi.runningTime)
        movieSummaryView.text = movieScheduleUi.summary
    }
}
