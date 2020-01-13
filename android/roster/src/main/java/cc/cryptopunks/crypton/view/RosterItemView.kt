package cc.cryptopunks.crypton.view

import android.content.Context
import android.graphics.drawable.GradientDrawable
import androidx.core.content.ContextCompat
import cc.cryptopunks.crypton.RosterItemService
import cc.cryptopunks.crypton.chat.R
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Presence
import cc.cryptopunks.crypton.context.Route
import cc.cryptopunks.crypton.context.Service
import cc.cryptopunks.crypton.util.bindings.clicks
import cc.cryptopunks.crypton.util.ext.inflate
import cc.cryptopunks.crypton.util.letterColors
import cc.cryptopunks.crypton.util.presenceStatusColors
import cc.cryptopunks.crypton.util.typedLog
import cc.cryptopunks.crypton.widget.ServiceLayout
import kotlinx.android.synthetic.main.roster_item.view.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.DateFormat

class RosterItemView(
    context: Context,
    private val dateFormat: DateFormat
) :
    ServiceLayout(context) {

    private val log = typedLog()

    private val statusColors: Map<Presence.Status, Int> = context.presenceStatusColors()

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
        log.d("init")
    }

    override fun Service.Binding.bind(): Job = launch {
        launch {
            input.collect {
                handleInput(it)
            }
        }
        launch {
            clicks().collect {
                Route.Chat().out()
            }
        }
    }

    private fun handleInput(input: Any) {
        if (input !is Service.Result<*>) return
        val state = input.state as? RosterItemService.State ?: return
        setDefaults(state)
        setMessage(state)
        setPresence(state)
    }

    private fun setDefaults(state: RosterItemService.State) {
        conversationTitleTextView.text = state.title
        conversationLetter.text = state.letter.toString()
        avatarDrawable.setColor(
            ContextCompat.getColor(
                context,
                letterColors.getValue(state.letter)
            )
        )
    }

    private fun setMessage(state: RosterItemService.State) {
        lastMessageTextView.text = state.message.formatMessage()
        dateTextView.text = dateFormat.format(state.message.timestamp)
    }

    private fun Message.formatMessage() = "${from.address.local}: $text"

    private fun setPresence(state: RosterItemService.State) {
        statusDrawable.setColor(statusColors.getValue(state.presence))
    }
}