package woowacourse.movie.movieTicket

import android.view.View
import android.widget.TextView
import woowacourse.movie.R
import woowacourse.movie.uimodel.MovieTicketUi
import woowacourse.movie.utils.DateUtil
import woowacourse.movie.view.decimalFormat

class MovieTicketView(
    private val view: View,
) {
    private val ticketTitleView: TextView
    private val ticketCountView: TextView
    private val ticketMovieReleaseDateView: TextView
    private val ticketTotalPriceView: TextView

    init {
        ticketTitleView = view.findViewById(R.id.ticket_movie_title)
        ticketCountView = view.findViewById(R.id.ticket_total_ticket_count)
        ticketMovieReleaseDateView = view.findViewById(R.id.ticket_release_date)
        ticketTotalPriceView = view.findViewById(R.id.ticket_total_price)
    }

    fun bind(movieTicketUi: MovieTicketUi) {
        val seats = movieTicketUi.seats.joinToString(", ") { it }

        ticketTitleView.text = movieTicketUi.title
        ticketCountView.text = view.context.getString(R.string.movie_ticket_count, movieTicketUi.count.toInt())
        ticketTotalPriceView.text = view.context.getString(R.string.movie_ticket_receipt, decimalFormat.format(movieTicketUi.totalPrice), seats)
        "${DateUtil(view.context).getDate(movieTicketUi.date)} ${DateUtil(view.context).getTime(movieTicketUi.time)}"
            .also { ticketMovieReleaseDateView.text = it }
    }
}
