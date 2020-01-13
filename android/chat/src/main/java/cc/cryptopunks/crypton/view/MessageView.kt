package cc.cryptopunks.crypton.view

import android.content.Context
import android.view.ContextMenu
import android.view.Gravity
import android.view.MenuInflater
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import cc.cryptopunks.crypton.chat.R
import cc.cryptopunks.crypton.context.Service
import cc.cryptopunks.crypton.service.MessageService
import cc.cryptopunks.crypton.util.ext.inflate
import cc.cryptopunks.crypton.widget.ServiceLayout
import kotlinx.android.synthetic.main.chat_message_item.view.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.DateFormat

class MessageView(
    context: Context,
    type: Int,
    private val dateFormat: DateFormat
) :
    ServiceLayout(
        if (type == Gravity.RIGHT) context
        else ContextThemeWrapper(
            context,
            R.style.Theme_Crypton_Colored
        )
    ) {

    private val padding by lazy { resources.getDimensionPixelSize(R.dimen.message_padding) }

    private val optionClickBroadcast = BroadcastChannel<Any>(1)

    init {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        inflate(R.layout.chat_message_item, true)
        setGravity(type)
        setOnLongClickListener { showContextMenu() }
    }

    override fun onCreateContextMenu(menu: ContextMenu) {
        MenuInflater(context)
            .inflate(R.menu.message, menu)
        menu.setHeaderTitle(R.string.choose_option_label)
            .findItem(R.id.copyToClipboard).setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.copyToClipboard -> MessageService.Copy
                    else -> null
                }?.let { action ->
                    optionClickBroadcast.offer(action)
                }
                true
            }
    }

    private fun setGravity(gravity: Int) = apply {
        when (gravity) {
            Gravity.LEFT -> setPadding(0, 0, padding, 0)
            Gravity.RIGHT -> setPadding(padding, 0, 0, 0)
            else -> throw Exception("Unsupported gravity")
        }
        linearLayout.gravity = gravity
        cardContainer.gravity = gravity
    }

    override fun Service.Binding.bind(): Job = launch {
        launch {
            input.collect { arg ->
                setState(arg)
            }
        }
        launch {
            optionClickBroadcast.asFlow().collect {
                it.out()
            }
        }
    }

    private fun setState(state: Any) {
        if (state is MessageService.State) state.run {
            bodyTextView.text = message
            authorTextView.text = " $BULLET $author"
            authorTextView.text = " $BULLET $status"
            timestampTextView.text = dateFormat.format(date)
        }
    }

    private companion object {
        const val BULLET = '•'
    }
}