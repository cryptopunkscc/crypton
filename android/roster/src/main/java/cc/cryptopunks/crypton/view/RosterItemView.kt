package cc.cryptopunks.crypton.view

import android.content.Context
import android.widget.FrameLayout
import cc.cryptopunks.crypton.chat.R
import cc.cryptopunks.crypton.entity.Message
import cc.cryptopunks.crypton.entity.Presence
import cc.cryptopunks.crypton.presenter.RosterItemPresenter
import cc.cryptopunks.crypton.util.bindings.clicks
import cc.cryptopunks.crypton.util.ext.inflate
import cc.cryptopunks.crypton.util.letterColors
import kotlinx.android.synthetic.main.roster_item.view.*
import kotlinx.coroutines.flow.Flow
import java.text.DateFormat

class RosterItemView(
    context: Context,
    private val dateFormat: DateFormat
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

    override val setMessage: suspend (Message) -> Unit
        get() = { message ->
            lastMessageTextView.text = message.text
            dateTextView.text = dateFormat.format(message.timestamp)
        }

    override val setPresence: suspend (presence: Presence.Status) -> Unit
        get() = { presence ->
            presenceTextView.text = presence.toString()
        }

    override val onClick: Flow<Any> get() = clicks()

    fun clear() {
        setLetter('a')
        setTitle("")
    }
}