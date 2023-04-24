package woowacourse.movie.movieTicket

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import woowacourse.movie.R
import woowacourse.movie.extension.getSerializableTicketOrNull
import woowacourse.movie.uimodel.MovieTicketUi

class MovieTicketActivity : AppCompatActivity() {

    private val movieTicketUi by lazy {
        intent.getSerializableTicketOrNull()
            ?: run {
                finish()
                MovieTicketUi.EMPTY
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_ticket)
        initToolBar()

        val ticketLayout = findViewById<ConstraintLayout>(R.id.ticket_layout)
        MovieTicketView(ticketLayout).apply {
            bind(movieTicketUi)
        }
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

    companion object {
        const val KEY_MOVIE_TICKET = "movieTicketUi"
    }
}
