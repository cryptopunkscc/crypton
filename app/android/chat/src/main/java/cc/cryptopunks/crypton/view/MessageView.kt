package cc.cryptopunks.crypton.view

import android.content.Context
import android.view.*
import android.widget.FrameLayout
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.view.iterator
import cc.cryptopunks.crypton.chat.R
import cc.cryptopunks.crypton.context.Exec
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

    val optionClicks = BroadcastChannel<Any>(1)

    var message: Message? = null
        set(value) {
            field = value?.apply {
                when {
                    value.status == Message.Status.State && value.text == Message.State.composing.name -> {
                        bodyTextView.text = "..."
                        authorTextView.text = author
                        statusTextView.text = null
                        timestampTextView.text = null
                        encryptedIcon.visibility = View.GONE
                    }
                    else -> {
                        bodyTextView.text = text
                        timestampTextView.text = dateFormat.format(timestamp)
                        authorTextView.text = " $BULLET $author"
                        statusTextView.text = StringBuffer(" $BULLET $status").apply {
                            if (encrypted) append(" $BULLET ")
                        }
                        encryptedIcon.visibility = if (encrypted) View.VISIBLE else View.GONE
                    }
                }
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
        menu.iterator().forEach { it.setOnMenuItemClickListener(onMenuItemCharSequence) }
    }

    private val onMenuItemCharSequence = MenuItem.OnMenuItemClickListener { item ->
        when (item.itemId) {
            R.id.copyToClipboard -> Exec.Copy(message!!)
            R.id.delete -> Exec.DeleteMessage(message!!)
            else -> null
        }?.let {
            optionClicks.offer(it)
            true
        } ?: false
    }

    private fun setGravity(gravity: Int) = apply {
        when (gravity) {
            Gravity.LEFT -> setPadding(0, 0, padding, 0)
            Gravity.RIGHT -> setPadding(padding, 0, 0, 0)
            else -> throw Exception("Unsupported gravity $gravity")
        }
        linearLayout.gravity = gravity
        cardContainer.gravity = gravity
    }

    private companion object {
        const val BULLET = '•'
    }
}
