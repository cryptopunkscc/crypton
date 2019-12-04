package cc.cryptopunks.crypton.presenter

import androidx.paging.PagedList
import cc.cryptopunks.crypton.context.*
import cc.cryptopunks.crypton.interactor.SendMessageInteractor
import cc.cryptopunks.crypton.selector.MessagePagedListSelector
import cc.cryptopunks.crypton.util.ext.map
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class ChatService @Inject constructor(
    val chat: Chat,
    override val scope: Feature.Scope,
    private val sendMessage: SendMessageInteractor,
    private val createMessagePresenter: MessagePresenter.Factory,
    private val messageFlow: MessagePagedListSelector,
    private val clipboardRepo: Clip.Board.Repo
) :
    Service.Abstract(),
    Message.Consumer {

    interface Input {
        data class SendMessage(val text: String) : Input
    }

    interface Output {
        data class Messages(val list: PagedList<MessagePresenter>) : Output
        data class MessageText(val text: CharSequence?) : Output
    }

    private val log = typedLog()

    override fun onInvoke() {
        scope.run {
            launch {
                clipboardRepo.pop()?.run { Output.MessageText(data).out() }
            }
            launch {
                messageFlow(chat, createMessagePresenter)
                    .onEach { log.d("received ${it.size} messages") }
                    .map(Output::Messages)
                    .collect(out)
            }.invokeOnCompletion {
                log.d("Message flow completed")
                it?.let(log::e)
            }
        }
    }

    override suspend fun Any.onInput() {
        when (this) {
            is Input.SendMessage -> if (text.isNotBlank()) sendMessage(text)
        }
    }

    override fun canConsume(message: Message): Boolean =
        message.chatAddress == chat.address


    interface Core {
        val chatService: ChatService
    }
}