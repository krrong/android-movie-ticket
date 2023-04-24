package woowacourse.movie.movieReservation

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import woowacourse.movie.R
import woowacourse.movie.extension.getSerializableScheduleOrNull
import woowacourse.movie.movieSeat.MovieSeatActivity
import woowacourse.movie.uimodel.MovieModelUi

class MovieReservationActivity : AppCompatActivity() {

    private val movieScheduleUi by lazy {
        intent.getSerializableScheduleOrNull()
            ?: run {
                finish()
                MovieModelUi.MovieScheduleUi.EMPTY
            }
    }

    private val scrollView by lazy { MovieDetailView(findViewById<ScrollView>(R.id.reservation_detail_view)) }
    private val bottomView by lazy {
        MovieBottomView(findViewById<ConstraintLayout>(R.id.reservation_bottom_view))
            .apply {
                registerReservationButton { startActivity(makeIntent()) }
                registerSpinnerListener(movieScheduleUi)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_reservation)

        scrollView.initMovieView(movieScheduleUi)
        bottomView.initBottomView()

        initToolBar()
        initInstanceState(savedInstanceState)
    }

    private fun initInstanceState(savedInstanceState: Bundle?) {
        savedInstanceState?.let {
            bottomView.setTicketCount(it.getInt(KEY_COUNT))
            bottomView.setSelectedPosition(it.getInt(KEY_TIME))
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_COUNT, bottomView.getTicketCount())
        outState.putInt(KEY_TIME, bottomView.getSelectedItemPosition())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initToolBar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun makeIntent(): Intent {
        val intent = Intent(this, MovieSeatActivity::class.java)
        val movieDetailUi = bottomView.makeMovieDetailUi(movieScheduleUi)
        intent.putExtra(MovieSeatActivity.KEY_MOVIE_DETAIL, movieDetailUi)
        return intent
    }

    companion object {
        private const val KEY_COUNT = "count"
        private const val KEY_TIME = "time"
        const val KEY_MOVIE_SCHEDULE = "movieScheduleUi"
    }
}
