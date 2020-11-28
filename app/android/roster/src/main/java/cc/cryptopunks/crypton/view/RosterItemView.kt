package cc.cryptopunks.crypton.view

import android.animation.LayoutTransition
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.roster.R
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Presence
import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.context.sender
import cc.cryptopunks.crypton.util.ext.inflate
import cc.cryptopunks.crypton.util.letterColors
import cc.cryptopunks.crypton.util.presenceStatusColors
import cc.cryptopunks.crypton.util.logger.typedLog
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
        log.builder.d { status = "Init" }
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

                statusDrawable.setColor(statusColors.getValue(presence))

                unreadMessagesTextView.text =
                    if (unreadMessagesCount > 0)
                        "+ $unreadMessagesCount" else
                        null

                message.run {
                    textViewModel().let { (visibility, message) ->
                        lastMessageTextView.text = message
                        lastMessageTextView.visibility = visibility
                    }

                    dateViewModel().let { (visibility, date) ->
                        dateTextView.text = date
                        dateTextView.visibility = visibility
                    }
                }

                visibility = View.VISIBLE
            }
        }

    private fun Message.textViewModel() = when {
        body.isEmpty() -> View.GONE to null
        from.address == Address.Empty -> View.GONE to null
        else -> View.VISIBLE to "$sender: $body"
    }

    private fun Message.dateViewModel() = when {
        timestamp == 0L -> View.GONE to null
        else -> View.VISIBLE to dateFormat.format(timestamp)
    }
}
