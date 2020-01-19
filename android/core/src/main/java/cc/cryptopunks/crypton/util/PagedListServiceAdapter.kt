package cc.cryptopunks.crypton.util

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import cc.cryptopunks.crypton.context.Service
import cc.cryptopunks.crypton.service.ServiceManager
import cc.cryptopunks.crypton.util.ext.invokeOnClose
import kotlinx.coroutines.CoroutineScope

abstract class PagedListServiceAdapter<V, S>(
    diffCallback: DiffUtil.ItemCallback<S> = DiffItemCallback()
) :
    PagedListAdapter<S, PagedListServiceAdapter<V, S>.ViewHolder>(diffCallback),
    CoroutineScope where
V : Service,
V : View,
S : Service {

    private val serviceManager = ServiceManager()

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        invokeOnClose {
            serviceManager.clear()
        }
    }

    abstract fun createView(parent: ViewGroup, viewType: Int): V

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(createView(parent, viewType))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.slot2 = getItem(position)
    }

    open class DiffItemCallback<S : Service> : DiffUtil.ItemCallback<S>() {
        override fun areItemsTheSame(
            oldItem: S,
            newItem: S
        ) = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: S,
            newItem: S
        ) = areItemsTheSame(oldItem, newItem)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = serviceManager.createBinding().apply { slot1 = view as Service }
    }
}