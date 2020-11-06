package cc.cryptopunks.crypton.widget

import android.view.View
import androidx.recyclerview.widget.RecyclerView

open class GenericViewHolder<V : View>(view: View) : RecyclerView.ViewHolder(view) {
    val view: V get() = itemView as V
}
