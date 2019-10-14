package cc.cryptopunks.crypton.feature.chat.presenter

import cc.cryptopunks.crypton.actor.Actor
import cc.cryptopunks.crypton.entity.Chat
import cc.cryptopunks.crypton.entity.Message
import cc.cryptopunks.crypton.feature.chat.selector.LastMessageSelector
import cc.cryptopunks.crypton.navigation.Navigate
import cc.cryptopunks.crypton.navigation.Route
import cc.cryptopunks.crypton.presenter.Presenter
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class RosterItemPresenter @Inject constructor(
    private val chat: Chat,
    private val navigate: Navigate,
    private val lastMessage: LastMessageSelector
) : Presenter<RosterItemPresenter.View> {

    val id get() = chat.address.id

    private val title get() = chat.title

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
            launch { onClick.collect(navigateChat) }
        }
    }

    interface View : Actor {
        fun setTitle(title: String)
        fun setLetter(letter: Char)
        val setMessage: suspend (message: Message) -> Unit
        val onClick: Flow<Any>
    }

    class Factory @Inject constructor(
        private val lastMessage: LastMessageSelector,
        private val navigate: Navigate
    ) : (Chat) -> RosterItemPresenter by { chat ->
        RosterItemPresenter(
            chat = chat,
            lastMessage = lastMessage,
            navigate = navigate
        )
    }
}