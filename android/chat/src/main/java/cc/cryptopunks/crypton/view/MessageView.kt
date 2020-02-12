package cc.cryptopunks.crypton.view

import android.content.Context
import android.view.ContextMenu
import android.view.Gravity
import android.view.MenuInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.view.ContextThemeWrapper
import cc.cryptopunks.crypton.chat.R
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.util.ext.inflate
import kotlinx.android.synthetic.main.chat_message_item.view.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BroadcastChannel
import java.text.DateFormat

class MessageView(
    context: Context,
    type: Int,
    private val dateFormat: DateFormat
) :
    FrameLayout(
        if (type == Gravity.RIGHT) context
        else ContextThemeWrapper(
            context,
            R.style.Theme_Crypton_Colored
        )
    ) {

    private val padding by lazy { resources.getDimensionPixelSize(R.dimen.message_padding) }

    val optionClicks = BroadcastChannel<Chat.Service.Option>(1)

    var message: Message? = null
        set(value) {
            field = value?.apply {
                bodyTextView.text = text
                authorTextView.text = " $BULLET $author"
                statusTextView.text = " $BULLET $status"
                timestampTextView.text = dateFormat.format(timestamp)
            }
        }

    var job: Job? = null

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
        MenuInflater(context).inflate(R.menu.message, menu)
        menu.setHeaderTitle(R.string.choose_option_label)
            .findItem(R.id.copyToClipboard)
            .setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.copyToClipboard -> Chat.Service.Option.Copy(message!!)
                    else -> null
                }?.let {
                    optionClicks.offer(it)
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

    private companion object {
        const val BULLET = '•'
    }
}