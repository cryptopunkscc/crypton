package cc.cryptopunks.crypton.handler

import androidx.paging.PagedList
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Session
import cc.cryptopunks.crypton.context.handle
import cc.cryptopunks.crypton.selector.MessagePagedListFlowSelector
import cc.cryptopunks.crypton.util.TypedLog
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

internal fun Session.handlePageMessagesSubscription(
    chat: Chat,
    messageFlow: MessagePagedListFlowSelector,
    log: TypedLog
) = handle<Chat.Service.SubscribePagedMessages> { output ->
    scope.launch {
        messageFlow(chat)
            .onEach(log.pagedMessagesReceived)
            .map { Chat.Service.PagedMessages(address, it) }
            .collect(output)
    }
}

private val TypedLog.pagedMessagesReceived: suspend (PagedList<Message>) -> Unit get() =
    { list -> d("Received ${list.size} messages") }
