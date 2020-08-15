package cc.cryptopunks.crypton.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cc.cryptopunks.crypton.debug.R
import cc.cryptopunks.crypton.util.Log

internal class DebugAdapter : RecyclerView.Adapter<LogEventViewHolder>() {

    val items = mutableListOf<Log.Event>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        LogEventViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.log_event,
                parent,
                false
            )
        )

    operator fun plusAssign(event: Log.Event) {
        items.add(event)
        notifyDataSetChanged()
        notifyItemInserted(items.lastIndex)
    }

    override fun getItemCount(): Int =
        items.size

    override fun onBindViewHolder(holder: LogEventViewHolder, position: Int) {
        holder.item = items[position]
    }
}
