package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.*
import cc.cryptopunks.crypton.context.Chat.Service.*
import cc.cryptopunks.crypton.interactor.MarkMessagesAsRead
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
    private val account: Address,
    private val sendMessage: SendMessageInteractor,
    private val markMessagesAsRead: MarkMessagesAsRead,
    private val messageFlow: MessagePagedListSelector,
    private val clipboardRepo: Clip.Board.Repo,
    private val copyToClipboard: Clip.Board.Sys.SetClip
) :
    Chat.Service,
    Message.Consumer {

    override val coroutineContext = SupervisorJob() + Dispatchers.IO

    private val log = typedLog()

    private var actorStatus: Any = Actor.Stop

    override fun Connector.connect(): Job = launch {
        launch {
            input.collect {
                log.d("in: $it")
                when (it) {
                    is Actor.Stop -> {
                        actorStatus = it
                    }
                    is Actor.Start -> {
                        actorStatus = it
                        setTextFromClipboard()
                    }
                    is Actor.Connected -> {
                        setTextFromClipboard()
                    }
                    is MessagesRead -> {
                        markMessagesAsRead(it.messages)
                    }
                    is SendMessage -> if (it.text.isNotBlank()) {
                        sendMessage(it.text)
                    }
                    is Option.Copy -> copyToClipboard(it.message.text)
                }
            }
        }
        launch {
            messageFlow(chat)
                .onEach { log.d("received ${it.size} messages") }
                .map { Messages(account, it) }
                .collect(output)
        }.invokeOnCompletion {
            log.d("Message flow completed")
            it?.let(log::e)
        }
    }

    private suspend fun Connector.setTextFromClipboard() {
        clipboardRepo.pop()?.run {
            output(MessageText(data))
        }
    }

    override fun canConsume(message: Message): Boolean =
        message.chatAddress == chat.address && actorStatus == Actor.Start


    interface Core {
        val chatService: ChatService
    }
}