package cc.cryptopunks.crypton.adapter

import android.view.Gravity
import android.view.ViewGroup
import cc.cryptopunks.crypton.service.MessageService
import cc.cryptopunks.crypton.util.PagedListServiceAdapter
import cc.cryptopunks.crypton.view.MessageView
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class MessageAdapter @Inject constructor(
    override val coroutineContext: CoroutineContext
) :
    PagedListServiceAdapter<MessageView, MessageService>(Diff) {

    private val dateFormat = SimpleDateFormat(
        "d MMM • HH:mm",
        Locale.getDefault()
    )

    override fun createView(parent: ViewGroup, viewType: Int) = MessageView(
        context = parent.context,
        type = viewType,
        dateFormat = dateFormat
    )

    override fun getItemViewType(position: Int): Int =
        if ((getItem(position))?.isAccountMessage != false)
            Gravity.RIGHT else
            Gravity.LEFT

    private object Diff : DiffItemCallback<MessageService>() {
        override fun areContentsTheSame(
            oldItem: MessageService,
            newItem: MessageService
        ) = oldItem.message == newItem.message
    }
}