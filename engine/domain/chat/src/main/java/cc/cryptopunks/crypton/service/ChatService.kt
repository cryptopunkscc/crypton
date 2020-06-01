package cc.cryptopunks.crypton.service

import androidx.paging.PagedList
import cc.cryptopunks.crypton.context.*
import cc.cryptopunks.crypton.context.Chat.Service.*
import cc.cryptopunks.crypton.interactor.MarkMessagesAsRead
import cc.cryptopunks.crypton.interactor.SaveActorStatusInteractor
import cc.cryptopunks.crypton.interactor.SendMessageInteractor
import cc.cryptopunks.crypton.module.HandlerRegistry
import cc.cryptopunks.crypton.module.dispatch
import cc.cryptopunks.crypton.selector.CanConsumeSelector
import cc.cryptopunks.crypton.selector.MessageListSelector
import cc.cryptopunks.crypton.selector.MessagePagedListFlowSelector
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
    private val messageFlow: MessagePagedListFlowSelector,
    private val messageList: MessageListSelector,
    private val popClipboardMessage: PopClipboardMessageSelector,
    private val canConsume: CanConsumeSelector,
    private val saveActorStatus: SaveActorStatusInteractor,
    private val clipboardSys: Clip.Board.Sys,
    private val handlers: HandlerRegistry
) :
    Connectable,
    Message.Consumer by canConsume {

    private val log = typedLog()

    override val coroutineContext = SupervisorJob() + Dispatchers.IO

    override fun Connector.connect(): Job = launch {
        input.collect {
            if (it is Actor.Status) saveActorStatus(it)
            when (it) {
                is Actor.Start,
                is Actor.Connected -> popClipboardMessage()?.out()
                is MessagesRead -> markMessagesAsRead(it.messages)
//                is SendMessage -> sendMessage(it.text)
                is Option.Copy -> clipboardSys.setClip(it.message.text)
                is Subscribe.PagedMessages -> collectPagedMessages(output)
                is Get.ListMessages -> messageList(chat).out()
                else -> handlers.dispatch(it, output)
            }
        }
    }

    private fun collectPagedMessages(output: suspend (PagedMessages) -> Unit) = launch {
        messageFlow(chat)
            .onEach(logPagedMessagesReceived)
            .map { PagedMessages(account, it) }
            .collect(output)
    }.invokeOnCompletion {
        log.d("Message flow completed")
        it?.let(log::e)
    }

    private val logPagedMessagesReceived: suspend (PagedList<Message>) -> Unit =
        { list -> log.d("Received ${list.size} messages") }
}

