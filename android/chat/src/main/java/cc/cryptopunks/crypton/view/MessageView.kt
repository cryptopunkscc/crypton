package cc.cryptopunks.crypton.view

import android.content.Context
import android.view.ContextMenu
import android.view.Gravity
import android.view.MenuInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.view.ContextThemeWrapper
import cc.cryptopunks.crypton.chat.R
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.presenter.MessagePresenter
import cc.cryptopunks.crypton.util.Broadcast
import cc.cryptopunks.crypton.util.ext.inflate
import kotlinx.android.synthetic.main.chat_message_item.view.*
import kotlinx.coroutines.flow.Flow
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
    ),
    MessagePresenter.View {

    private val padding by lazy { resources.getDimensionPixelSize(R.dimen.message_padding) }

    private val optionClickBroadcast = Broadcast<MessagePresenter.Option>()

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
            .findItem(R.id.copyToClipboard).setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.copyToClipboard -> MessagePresenter.Option.Copy
                    else -> null
                }?.let(optionClickBroadcast)
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

    override fun setMessage(text: String) {
        bodyTextView.text = text
    }

    override fun setAuthor(name: String) {
        authorTextView.text = " $BULLET $name"
    }

    override fun setStatus(status: Message.Status) {
        authorTextView.text = " $BULLET ${status.name}"
    }

    override val optionSelections: Flow<MessagePresenter.Option> get() = optionClickBroadcast

    override fun setDate(timestamp: Long) {
        timestampTextView.text = dateFormat.format(timestamp)
    }

    private companion object {
        const val BULLET = '•'
    }
}