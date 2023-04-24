package woowacourse.movie.movieReservation

import android.view.View
import android.view.View.OnClickListener
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import movie.data.TicketCount
import movie.screening.ScreeningTime
import woowacourse.movie.R
import woowacourse.movie.uimodel.MovieDetailUi
import woowacourse.movie.uimodel.MovieModelUi
import woowacourse.movie.utils.toDomain
import java.time.LocalDate
import java.time.LocalTime

class MovieBottomView(
    private val view: View,
) {

    private val dateSpinner by lazy { view.findViewById<Spinner>(R.id.reservation_screening_date_spinner) }
    private val timeSpinner by lazy { view.findViewById<Spinner>(R.id.reservation_screening_time_spinner) }
    private val ticketCountView by lazy { view.findViewById<TextView>(R.id.reservation_ticket_count) }
    private var ticketCount = TicketCount(1)
    private var selectedPosition = 0

    fun initBottomView() {
        ticketCountView.text = ticketCount.toInt().toString()
        registerCountButton()
    }

    private fun registerCountButton() {
        val decreaseButton = view.findViewById<TextView>(R.id.reservation_decrease_ticket_button)
        val increaseButton = view.findViewById<TextView>(R.id.reservation_increase_ticket_button)

        decreaseButton.setOnClickListener {
            ticketCountView.text = (--ticketCount).toString()
        }
        increaseButton.setOnClickListener {
            ticketCountView.text = (++ticketCount).toString()
        }
    }

    fun registerReservationButton(listener: OnClickListener) {
        val reservationButton = view.findViewById<TextView>(R.id.reservation_complete_button)
        reservationButton.setOnClickListener(listener)
    }

    fun registerSpinnerListener(movieScheduleUi: MovieModelUi.MovieScheduleUi) {
        val dateList = movieScheduleUi.toDomain().getScreeningDate()
        val adapter = ArrayAdapter(view.context, android.R.layout.simple_spinner_item, dateList)
        dateSpinner.adapter = adapter

        dateSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                updateTimeView(LocalDate.parse(dateList[position]))
                timeSpinner.setSelection(selectedPosition)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }
    }

    private fun updateTimeView(date: LocalDate) {
        val timeList = ScreeningTime.getScreeningTime(date)
        val adapter = ArrayAdapter(view.context, android.R.layout.simple_spinner_item, timeList)
        timeSpinner.adapter = adapter
    }

    fun makeMovieDetailUi(movieScheduleUi: MovieModelUi.MovieScheduleUi): MovieDetailUi {
        val selectedDate = LocalDate.parse(dateSpinner.selectedItem.toString())
        val selectedTime = LocalTime.parse(timeSpinner.selectedItem.toString())

        return MovieDetailUi(
            title = movieScheduleUi.title,
            count = ticketCount,
            date = selectedDate,
            time = selectedTime,
        )
    }

    fun getTicketCount(): Int {
        return ticketCount.toInt()
    }

    fun setTicketCount(count: Int) {
        ticketCount = TicketCount(count)
        ticketCountView.text = count.toString()
    }

    fun getSelectedItemPosition(): Int {
        return timeSpinner.selectedItemPosition
    }

    fun setSelectedPosition(position: Int) {
        selectedPosition = position
    }
}
