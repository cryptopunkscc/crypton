package cc.cryptopunks.crypton.presenter

import cc.cryptopunks.crypton.actor.Actor
import cc.cryptopunks.crypton.entity.Chat
import cc.cryptopunks.crypton.entity.Message
import cc.cryptopunks.crypton.entity.Presence
import cc.cryptopunks.crypton.selector.LastMessageSelector
import cc.cryptopunks.crypton.navigation.Navigate
import cc.cryptopunks.crypton.navigation.Route
//import cc.cryptopunks.crypton.selector.PresenceSelector
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class RosterItemPresenter @Inject constructor(
    private val chat: Chat,
    private val navigate: Navigate,
//    private val presenceOf: PresenceSelector, // TODO: Presence
    private val lastMessage: LastMessageSelector
) : Presenter<RosterItemPresenter.View> {

    val id get() = chat.address.id

    private val title get() = chat.address.id

    private val letter get() = title.firstOrNull()?.toLowerCase() ?: '0'

    private val navigateChat: suspend (Any) -> Unit = {
        navigate(Route.Chat()) {
            accountId = chat.account.id
            chatAddress = chat.address.id
        }
    }

    override suspend fun View.invoke(): Job = coroutineScope {
        launch {
            setTitle(title)
            setLetter(letter)
            launch { lastMessage(chat).collect(setMessage) }
//            launch { presenceOf(chat.address).collect(setPresence) } // TODO: Presence
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
        private val lastMessage: LastMessageSelector,
//        private val presenceSelector: PresenceSelector, // TODO: Presence
        private val navigate: Navigate
    ) : (Chat) -> RosterItemPresenter by { chat ->
        RosterItemPresenter(
            chat = chat,
            lastMessage = lastMessage,
//            presenceOf = presenceSelector, // TODO: Presence
            navigate = navigate
        )
    }
}