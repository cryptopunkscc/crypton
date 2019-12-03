package cc.cryptopunks.crypton.presenter

import androidx.paging.PagedList
import cc.cryptopunks.crypton.context.*
import cc.cryptopunks.crypton.interactor.SendMessageInteractor
import cc.cryptopunks.crypton.selector.MessagePagedListSelector
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatPresenter @Inject constructor(
    val chat: Chat,
    private val sendMessage: SendMessageInteractor,
    private val createMessagePresenter: MessagePresenter.Factory,
    private val messageFlow: MessagePagedListSelector,
    private val clipboardRepo: Clip.Board.Repo
) :
    Presenter<ChatPresenter.Actor>,
    Message.Consumer {

    private val log = typedLog()

    private val send: suspend (String) -> Unit = { sendMessage(it) }

    override suspend fun Actor.invoke() = coroutineScope {
        launch { setInputMessage(clipboardRepo.pop()?.data) }
        launch { sendMessageFlow.filter { it.isNotBlank() }.collect(send) }
        launch {
            messageFlow(chat, createMessagePresenter)
                .onEach { log.d("received ${it.size} messages") }
                .collect(setMessages)
        }.invokeOnCompletion {
            log.d("Message flow completed")
            it?.let(log::e)
        }
    }

    override fun canConsume(message: Message): Boolean =
        message.chatAddress == chat.address

    interface Actor {
        val setInputMessage: (CharSequence?) -> Unit
        val sendMessageFlow: Flow<String>
        val setMessages: suspend (PagedList<MessagePresenter>) -> Unit
    }

    interface Core {
        val chatPresenter: ChatPresenter
    }
}