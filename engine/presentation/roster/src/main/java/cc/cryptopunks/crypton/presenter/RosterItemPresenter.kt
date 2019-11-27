package cc.cryptopunks.crypton.presenter

import cc.cryptopunks.crypton.actor.Actor
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Presence
import cc.cryptopunks.crypton.context.Route
import cc.cryptopunks.crypton.selector.LatestMessageFlowSelector
import cc.cryptopunks.crypton.selector.PresenceFlowSelector
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class RosterItemPresenter @Inject constructor(
    private val chat: Chat,
    private val navigate: Route.Api.Navigate,
    private val presenceOf: PresenceFlowSelector,
    private val latestMessageFlow: LatestMessageFlowSelector
) : Presenter<RosterItemPresenter.View> {

    val id get() = chat.address.id

    private val title get() = chat.address.id

    private val letter get() = title.firstOrNull()?.toLowerCase() ?: '0'

    private val navigateChat: suspend (Any) -> Unit = {
        Route.Chat().apply {
            accountId = chat.account.id
            chatAddress = chat.address.id
        }.let(navigate)
    }

    override suspend fun View.invoke(): Job = coroutineScope {
        launch {
            setTitle(title)
            setLetter(letter)
            launch { latestMessageFlow(chat).collect(setMessage) }
            launch { presenceOf(chat.address).collect(setPresence) }
            launch { onClick.collect(navigateChat) }
        }
    }

    interface View : Actor {
        fun setTitle(title: String)
        fun setLetter(letter: Char)
        val setMessage: suspend (message: Message) -> Unit
        val setPresence: suspend (presence: Presence.Status) -> Unit
        val onClick: Flow<Any>
    }

    class Factory @Inject constructor(
        private val latestMessageFlow: LatestMessageFlowSelector,
        private val presenceSelector: PresenceFlowSelector,
        private val navigate: Route.Api.Navigate
    ) : (Chat) -> RosterItemPresenter by { chat ->
        RosterItemPresenter(
            chat = chat,
            latestMessageFlow = latestMessageFlow,
            presenceOf = presenceSelector,
            navigate = navigate
        )
    }
}