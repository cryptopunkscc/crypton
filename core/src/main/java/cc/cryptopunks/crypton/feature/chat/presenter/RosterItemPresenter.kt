package cc.cryptopunks.crypton.feature.chat.presenter

import cc.cryptopunks.crypton.entity.Chat
import cc.cryptopunks.crypton.entity.Message
import cc.cryptopunks.crypton.feature.Route
import cc.cryptopunks.crypton.feature.chat.selector.LastMessageSelector
import cc.cryptopunks.crypton.util.Navigate
import cc.cryptopunks.crypton.util.Presenter
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

    val id = chat.id

    private val title get() = chat.title

    private val letter get() = title.first().toLowerCase()

    private val navigateChat: suspend (Any) -> Unit = { navigate(Route.Chat(id)) }

    override suspend fun View.invoke() = coroutineScope {
        setTitle(title)
        setLetter(letter)
        launch { lastMessage(chat).collect(setMessage) }
        launch { onClick.collect(navigateChat) }
    }

    interface View {
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