package cc.cryptopunks.crypton.service

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
    private val createMessageService: MessageService.Factory,
    private val messageFlow: MessagePagedListSelector,
    private val clipboardRepo: Clip.Board.Repo
) :
    Service,
    Message.Consumer {

    data class SendMessage(val text: String)
    data class Messages(val list: PagedList<MessageService>)
    data class MessageText(val text: CharSequence?)

    override val coroutineContext = SupervisorJob() + Dispatchers.IO

    private val log = typedLog()

    override fun Service.Connector.connect(): Job = launch {
        launch {
            input.collect {
                if (it is SendMessage && it.text.isNotBlank())
                    sendMessage(it.text)
            }
        }
        launch {
            clipboardRepo.pop()?.run {
                output(MessageText(data))
            }
        }
        launch {
            messageFlow(chat, createMessageService)
                .onEach { log.d("received ${it.size} messages") }
                .map(::Messages)
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