package cc.cryptopunks.crypton.view

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import cc.cryptopunks.crypton.chat.R
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Presence
import cc.cryptopunks.crypton.presenter.RosterItemPresenter
import cc.cryptopunks.crypton.util.bindings.clicks
import cc.cryptopunks.crypton.util.ext.inflate
import cc.cryptopunks.crypton.util.letterColors
import cc.cryptopunks.crypton.util.presenceStatusColors
import kotlinx.android.synthetic.main.roster_item.view.*
import kotlinx.coroutines.flow.Flow
import java.text.DateFormat

class RosterItemView(
    context: Context,
    private val dateFormat: DateFormat
) :
    FrameLayout(context),
    RosterItemPresenter.Actor {

    private val statusColors = context.presenceStatusColors()

    private val avatarDrawable by lazy {
        conversationLetter.background as GradientDrawable
    }
    private val statusDrawable by lazy {
        presenceStatusIcon.drawable as GradientDrawable
    }

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
        conversationLetter.text = letter.toString()
        avatarDrawable.setColor(ContextCompat.getColor(context, letterColors.getValue(letter)))
    }

    override val setMessage: suspend (Message) -> Unit
        get() = { message ->
            lastMessageTextView.text = format(message)
            dateTextView.text = dateFormat.format(message.timestamp)
        }

    private fun format(message: Message) = message.run {
        "${from.address.local}: $text"
    }

    override val setPresence: suspend (presence: Presence.Status) -> Unit
        get() = { presence ->
            statusDrawable.setColor(statusColors.getValue(presence))
        }

    override val onClick: Flow<Any> get() = clicks()

    fun clear() {
        setLetter('a')
        setTitle("")
    }
}