package cc.cryptopunks.crypton.view

import android.content.Context
import android.widget.FrameLayout
import cc.cryptopunks.crypton.chat.R
import cc.cryptopunks.crypton.entity.Message
import cc.cryptopunks.crypton.feature.chat.presenter.RosterItemPresenter
import cc.cryptopunks.crypton.util.bindings.clicks
import cc.cryptopunks.crypton.util.ext.inflate
import cc.cryptopunks.crypton.util.letterColors
import kotlinx.android.synthetic.main.roster_item.view.*
import kotlinx.coroutines.flow.Flow
import java.util.*

class RosterItemView(
    context: Context
) :
    FrameLayout(context),
    RosterItemPresenter.View {

    init {
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )
        inflate(R.layout.roster_item, true)
    }

    override fun setTitle(title: String) {
        conversationTitleTextView.text = title
    }

    override fun setLetter(letter: Char) {
        conversationLetter.apply {
            text = letter.toString()
            setBackgroundResource(letterColors.getValue(letter))
        }
    }

    override val setMessage: suspend (Message) -> Unit get() = { message ->
        lastMessageTextView.text = message.text
        dateTextView.text = Date(message.timestamp).toString()
    }

    override val onClick: Flow<Any> get() = clicks()

    fun clear() {
        setLetter('a')
        setTitle("")
    }
}