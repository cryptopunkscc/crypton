package cc.cryptopunks.crypton.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.FrameLayout
import cc.cryptopunks.crypton.chat.R
import cc.cryptopunks.crypton.feature.chat.presenter.MessagePresenter
import cc.cryptopunks.crypton.util.ext.inflate
import kotlinx.android.synthetic.main.chat_message_item.view.*
import java.util.*

class MessageView(
    context: Context
) :
    FrameLayout(context),
    MessagePresenter.View {

    init {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        inflate(R.layout.chat_message_item, true)
    }

    override fun setAuthor(name: String) {
        authorTextView.text = name
    }

    override fun setMessage(text: String) {
        bodyTextView.text = text
    }

    override fun setDate(timestamp: Long) {
        timestampTextView.text = Date(timestamp).toString()
    }

    fun clear() {
        setMessage("")
        setDate(0)
    }
}