package woowacourse.movie.movieSeat

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TableLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import movie.discountpolicy.NormalDiscountPolicy
import movie.seat.Seat
import woowacourse.movie.R
import woowacourse.movie.extension.getSerializableMovieDetailOrNull
import woowacourse.movie.movieTicket.MovieTicketActivity
import woowacourse.movie.uimodel.MovieDetailUi
import woowacourse.movie.uimodel.MovieTicketUi
import woowacourse.movie.utils.toDomain
import woowacourse.movie.utils.toUi

class MovieSeatActivity : AppCompatActivity() {

    private val movieDetail by lazy {
        (intent.getSerializableMovieDetailOrNull())
            ?: run {
                finish()
                MovieDetailUi.EMPTY
            }
    }

    private val nextButton by lazy { findViewById<Button>(R.id.seat_next_button) }
    private val priceTextView by lazy { findViewById<TextView>(R.id.seat_ticket_price) }

    private val selectedSeats: MutableList<Seat> = mutableListOf()
    private var totalPrice: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_seat)

        initMovieView()
        initListener()
    }

    private fun initMovieView() {
        val seatTableLayout = findViewById<TableLayout>(R.id.seat_table)
        MovieSeatView(seatTableLayout, ::selectSeat)

        val movieTitleView = findViewById<TextView>(R.id.seat_movie_title)
        movieTitleView.text = movieDetail.title

        nextButton.setOnClickListener {
            val dialog = makeDialog()
            dialog.show()
        }
    }

    private fun makeDialog(): AlertDialog {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder
            .setTitle(getString(R.string.dialog_title))
            .setMessage(getString(R.string.dialog_message))
            .setCancelable(false)

        val listener = makeOnClickListener()

        dialogBuilder.setPositiveButton(getString(R.string.dialog_positive_message), listener)
        dialogBuilder.setNegativeButton(getString(R.string.dialog_negative_message), listener)

        return dialogBuilder.create()
    }

    private fun makeOnClickListener(): DialogInterface.OnClickListener {
        return DialogInterface.OnClickListener { _, p1 ->
            when (p1) {
                DialogInterface.BUTTON_POSITIVE -> { startActivity(makeIntent()) }
                DialogInterface.BUTTON_NEGATIVE -> Unit
            }
        }
    }

    private fun makeIntent(): Intent {
        return Intent(this, MovieTicketActivity::class.java).apply {
            val movieTicketUi = makeMovieTicketUi()
            putExtra(MovieTicketActivity.KEY_MOVIE_TICKET, movieTicketUi)
        }
    }

    private fun makeMovieTicketUi(): MovieTicketUi {
        return MovieTicketUi.of(
            totalPrice,
            movieDetail.toDomain(),
            selectedSeats.map { seat ->
                seat.toUi().getSeatPosition()
            },
        )
    }

    private fun initListener() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun selectSeat(seat: Seat, position: TextView) {
        when {
            !selectedSeats.contains(seat) && movieDetail.toDomain().isUpOfCount(selectedSeats.size) -> { selected(seat, position) }
            !selectedSeats.contains(seat) && !movieDetail.toDomain().isUpOfCount(selectedSeats.size) -> Unit
            else -> { deselected(seat, position) }
        }
        updateBottomView()
    }

    private fun selected(seat: Seat, positionTextView: TextView) {
        val discountPolicy = NormalDiscountPolicy(movieDetail.date, movieDetail.time)
        selectedSeats.add(seat)
        totalPrice += discountPolicy.getDiscountPrice(seat.getSeatPrice())
        positionTextView.isSelected = true
    }

    private fun deselected(seat: Seat, positionTextView: TextView) {
        val discountPolicy = NormalDiscountPolicy(movieDetail.date, movieDetail.time)
        selectedSeats.remove(seat)
        totalPrice -= discountPolicy.getDiscountPrice(seat.getSeatPrice())
        positionTextView.isSelected = false
    }

    private fun updateBottomView() {
        priceTextView.text = getString(R.string.total_price).format(totalPrice)
        nextButton.isEnabled = movieDetail.count.toInt() == selectedSeats.size
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

    companion object {
        const val KEY_MOVIE_DETAIL = "movieDetailUi"
    }
}
