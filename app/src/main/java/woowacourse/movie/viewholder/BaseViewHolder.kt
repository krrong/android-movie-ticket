package woowacourse.movie.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import woowacourse.movie.uimodel.MovieModelUi

abstract class BaseViewHolder(
    view: View,
) : RecyclerView.ViewHolder(view) {

    abstract fun <T : MovieModelUi> bind(item: T)
}
