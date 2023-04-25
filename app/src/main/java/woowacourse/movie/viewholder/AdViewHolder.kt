package woowacourse.movie.viewholder

import android.view.View
import android.widget.ImageView
import woowacourse.movie.R
import woowacourse.movie.uimodel.MovieModelUi

class AdViewHolder(
    val view: View,
) : BaseViewHolder(view) {

    private val movieImageView: ImageView = view.findViewById(R.id.ad_image_view)

    override fun <T : MovieModelUi> bind(item: T) {
        item as MovieModelUi.AdUi
        movieImageView.setImageResource(item.adPoster)
    }
}
