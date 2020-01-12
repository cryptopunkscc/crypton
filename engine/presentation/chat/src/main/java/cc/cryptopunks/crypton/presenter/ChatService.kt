package cc.cryptopunks.crypton.presenter

import androidx.paging.PagedList
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Clip
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Service
import cc.cryptopunks.crypton.interactor.SendMessageInteractor
import cc.cryptopunks.crypton.selector.MessagePagedListSelector
import cc.cryptopunks.crypton.util.ext.map
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatService @Inject constructor(
    val chat: Chat,
    private val sendMessage: SendMessageInteractor,
    private val createMessagePresenter: MessagePresenter.Factory,
    private val messageFlow: MessagePagedListSelector,
    private val clipboardRepo: Clip.Board.Repo
) :
    Service,
    Message.Consumer {

    interface Input {
        data class SendMessage(val text: String) : Input
    }

    interface Output {
        data class Messages(val list: PagedList<MessagePresenter>) : Output
        data class MessageText(val text: CharSequence?) : Output
    }

    override val coroutineContext = SupervisorJob() + Dispatchers.IO

    private val log = typedLog()

    override fun Service.Binding.bind(): Job = launch {
        launch {
            input.collect {
                if (it is Input.SendMessage && it.text.isNotBlank())
                    sendMessage(it.text)
            }
        }
        launch {
            clipboardRepo.pop()?.run {
                output(Output.MessageText(data))
            }
        }
        launch {
            messageFlow(chat, createMessagePresenter)
                .onEach { log.d("received ${it.size} messages") }
                .map(Output::Messages)
                .collect(output)
        }.invokeOnCompletion {
            log.d("Message flow completed")
            it?.let(log::e)
        }
    }

    override fun canConsume(message: Message): Boolean =
        message.chatAddress == chat.address


    interface Core {
        val chatService: ChatService
    }
}