package cc.cryptopunks.crypton.view

import android.animation.LayoutTransition
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import cc.cryptopunks.crypton.chat.R
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Presence
import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.util.ext.inflate
import cc.cryptopunks.crypton.util.letterColors
import cc.cryptopunks.crypton.util.presenceStatusColors
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.android.synthetic.main.roster_item.view.*
import java.text.DateFormat

class RosterItemView(
    context: Context,
    private val dateFormat: DateFormat
) :
    FrameLayout(context) {

    private val log = typedLog()

    private val statusColors: Map<Presence.Status, Int> = context.presenceStatusColors()

    private val avatarDrawable by lazy {
        conversationLetter.background as GradientDrawable
    }

    private val statusDrawable by lazy {
        presenceStatusIcon.drawable as GradientDrawable
    }

    init {
        visibility = View.INVISIBLE
        layoutTransition = LayoutTransition()
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )
        inflate(R.layout.roster_item, true)
        log.d("init")
    }

    var item: Roster.Item? = null
        set(value) {
            field = value?.apply {
                conversationTitleTextView.text = title

                conversationLetter.text = letter.toString()

                avatarDrawable.setColor(
                    ContextCompat.getColor(
                        context,
                        letterColors.getValue(letter)
                    )
                )

                lastMessageTextView.text = message.formatMessage()

                dateTextView.text = dateFormat.format(message.timestamp)

                statusDrawable.setColor(statusColors.getValue(presence))

                unreadMessagesTextView.text =
                    if (unreadMessagesCount > 0)
                        "+ $unreadMessagesCount" else
                        null

                visibility = View.VISIBLE
            }
        }

    private fun Message.formatMessage() = "${from.address.local}: $text"
}
