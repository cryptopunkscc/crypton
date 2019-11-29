package cc.cryptopunks.crypton.presenter

import androidx.paging.PagedList
import cc.cryptopunks.crypton.context.*
import cc.cryptopunks.crypton.interactor.SendMessageInteractor
import cc.cryptopunks.crypton.selector.MessagePagedListSelector
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatPresenter @Inject constructor(
    val chat: Chat,
    private val sendMessage: SendMessageInteractor,
    private val createMessagePresenter: MessagePresenter.Factory,
    private val messageFlow: MessagePagedListSelector,
    private val clipboardRepo: Clip.Board.Repo
) :
    Presenter<ChatPresenter.View>,
    Message.Consumer {

    private val send: suspend (String) -> Unit = { sendMessage(it) }

    override suspend fun View.invoke() = coroutineScope {
        launch { setInputMessage(clipboardRepo.pop()?.data) }
        launch { sendMessageFlow.collect(send) }
        launch { messageFlow(chat, createMessagePresenter).collect(setMessages) }
    }

    override fun canConsume(message: Message): Boolean =
        message.chatAddress == chat.address

    interface View : Actor {
        val sendMessageFlow: Flow<String>
        val setMessages: suspend (PagedList<MessagePresenter>) -> Unit
        val setInputMessage: (CharSequence?) -> Unit
    }

    interface Core {
        val chatPresenter: ChatPresenter
    }
}