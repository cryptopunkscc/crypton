package cc.cryptopunks.crypton.util

import android.content.Context
import androidx.recyclerview.widget.RecyclerView

class ScrollHelper(context: Context) {
    private val scrollThreshold: Int = context.resources.displayMetrics.run {
        scaledDensity * SCROLL_THRESHOLD_DP
    }.toInt()

    fun isBottomReached(view: RecyclerView) = view.run {
        val maxScroll = computeVerticalScrollRange()
        val currentScroll = computeVerticalScrollOffset() + computeVerticalScrollExtent()
        maxScroll - currentScroll < scrollThreshold
    }

    fun scrollToTop(view: RecyclerView) =
        view.smoothScrollToPosition(0)

    fun scrollToBottom(view: RecyclerView, smooth: Boolean = true) {
        val position = view.adapter!!.itemCount-1
        when (smooth) {
            true -> view.smoothScrollToPosition(position)
            false -> view.scrollToPosition(position)
        }
    }

    private companion object {
        const val SCROLL_THRESHOLD_DP = 100
    }
}
