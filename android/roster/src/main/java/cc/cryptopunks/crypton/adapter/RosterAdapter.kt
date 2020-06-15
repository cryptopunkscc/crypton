package cc.cryptopunks.crypton.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.view.RosterItemView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.CoroutineContext

class RosterAdapter(
    override val coroutineContext: CoroutineContext
) :
    RecyclerView.Adapter<RosterAdapter.ViewHolder>(),
    CoroutineScope {

    val clicks = BroadcastChannel<Roster.Service.Select>(Channel.BUFFERED)

    private val onClickListener = View.OnClickListener {
        (it as? RosterItemView)?.item?.let { item ->
            clicks.offer(Roster.Service.Select(item))
        }
    }

    private val dateFormat = SimpleDateFormat(
        "d MMM yyyy • HH:mm",
        Locale.getDefault()
    )

    private var list = emptyList<Roster.Item>()

    fun submitList(list: List<Roster.Item>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ViewHolder(RosterItemView(parent.context, dateFormat).apply {
        setOnClickListener(onClickListener)
    })

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.item = list[position]
    }

    class ViewHolder(view: RosterItemView) : RecyclerView.ViewHolder(view) {
        val view get() = itemView as RosterItemView
    }
}
