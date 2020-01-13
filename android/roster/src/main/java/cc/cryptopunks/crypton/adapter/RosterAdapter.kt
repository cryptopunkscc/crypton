package cc.cryptopunks.crypton.adapter

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import cc.cryptopunks.crypton.context.Service
import cc.cryptopunks.crypton.service.ServiceBindingManager
import cc.cryptopunks.crypton.util.ext.invokeOnClose
import cc.cryptopunks.crypton.view.RosterItemView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelChildren
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class RosterAdapter @Inject constructor(
    override val coroutineContext: CoroutineContext
) :
    PagedListAdapter<Service, RosterAdapter.ViewHolder>(Diff),
    CoroutineScope {

    private val serviceManager = ServiceBindingManager()

    private val dateFormat: DateFormat = SimpleDateFormat(
        "d MMM yyyy • HH:mm",
        Locale.getDefault()
    )

    init {
        invokeOnClose {
            serviceManager.clear()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(RosterItemView(parent.context, dateFormat))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.right = getItem(position)
    }

    private object Diff : DiffUtil.ItemCallback<Service>() {

        override fun areItemsTheSame(
            oldItem: Service,
            newItem: Service
        ) = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: Service,
            newItem: Service
        ) = areItemsTheSame(oldItem, newItem)
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        holder.binding.left
            ?.coroutineContext
            ?.cancelChildren()
    }

    inner class ViewHolder(view: RosterItemView) : RecyclerView.ViewHolder(view) {
        val binding = serviceManager.createBinding().apply { left = view }
    }
}