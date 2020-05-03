package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.*
import cc.cryptopunks.crypton.context.Chat.Service.*
import cc.cryptopunks.crypton.interactor.MarkMessagesAsRead
import cc.cryptopunks.crypton.interactor.SaveActorStatusInteractor
import cc.cryptopunks.crypton.interactor.SendMessageInteractor
import cc.cryptopunks.crypton.selector.CanConsumeSelector
import cc.cryptopunks.crypton.selector.MessagePagedListSelector
import cc.cryptopunks.crypton.selector.PopClipboardMessageSelector
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ChatService internal constructor(
    val chat: Chat,
    private val account: Address,
    private val sendMessage: SendMessageInteractor,
    private val markMessagesAsRead: MarkMessagesAsRead,
    private val messageFlow: MessagePagedListSelector,
    private val popClipboardMessage: PopClipboardMessageSelector,
    private val canConsume: CanConsumeSelector,
    private val saveActorStatus: SaveActorStatusInteractor,
    private val clipboardSys: Clip.Board.Sys
) :
    Chat.Service,
    Message.Consumer by canConsume {

    private val log = typedLog()

    override val coroutineContext = SupervisorJob() + Dispatchers.IO

    override fun Connector.connect(): Job = launch {
        launch {
            input.collect {
                if (it is Actor.Status) saveActorStatus(it)
                when (it) {
                    is Actor.Start,
                    is Actor.Connected -> popClipboardMessage()?.out()
                    is MessagesRead -> markMessagesAsRead(it.messages)
                    is SendMessage -> if (it.text.isNotBlank()) sendMessage(it.text)
                    is Option.Copy -> clipboardSys.setClip(it.message.text)
                }
            }
        }
        launch {
            messageFlow(chat)
                .onEach { log.d("Received ${it.size} messages") }
                .map { Messages(account, it) }
                .collect(output)
        }.invokeOnCompletion {
            log.d("Message flow completed")
            it?.let(log::e)
        }
    }
}
