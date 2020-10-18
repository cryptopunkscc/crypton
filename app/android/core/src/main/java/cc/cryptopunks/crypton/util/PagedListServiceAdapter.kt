package cc.cryptopunks.crypton.util

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import cc.cryptopunks.crypton.Actor
import cc.cryptopunks.crypton.Connectable
import cc.cryptopunks.crypton.cancelAll
import cc.cryptopunks.crypton.createBinding
import cc.cryptopunks.crypton.util.ext.invokeOnClose
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import kotlin.properties.Delegates

abstract class PagedListServiceAdapter<V, S>(
    diffCallback: DiffUtil.ItemCallback<S> = DiffItemCallback()
) :
    PagedListAdapter<S, PagedListServiceAdapter<V, S>.ViewHolder>(diffCallback),
    CoroutineScope where
V : Actor,
V : View,
S : Connectable {

    private val connectableBindingStore = Connectable.Binding.Store()

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        invokeOnClose {
            runBlocking { connectableBindingStore.cancelAll() }
        }
    }

    abstract fun createView(parent: ViewGroup, viewType: Int): V

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(createView(parent, viewType))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.item = getItem(position)
    }

    open class DiffItemCallback<S : Connectable> : DiffUtil.ItemCallback<S>() {
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
        private val binding: Connectable.Binding = connectableBindingStore.createBinding().apply {
            plus(view as Connectable)
        }
        var item by Delegates.observable<Connectable?>(null) { _, oldValue, newValue ->
            oldValue?.let { binding - it }
            binding + newValue
        }
    }
}
