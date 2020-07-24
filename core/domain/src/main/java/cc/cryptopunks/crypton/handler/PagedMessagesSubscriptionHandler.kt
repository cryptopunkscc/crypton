package cc.cryptopunks.crypton.handler

import androidx.paging.PagedList
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.ChatScope
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.handle
import cc.cryptopunks.crypton.selector.messagePagedListFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

internal fun handlePageMessagesSubscription() =
    handle { out, _: Chat.Service.SubscribePagedMessages ->
        messagePagedListFlow()
            .onEach(pagedMessagesReceived)
            .map { Chat.Service.PagedMessages(address, it) }
            .onEach { pagedMessage { it } }
            .collect(out)
    }

private val ChatScope.pagedMessagesReceived: suspend (PagedList<Message>) -> Unit
    get() = { list -> log.d("Received ${list.size} messages for chat $chat") }

internal fun handleGetPagedMessages() = handle { out, _: Chat.Service.GetPagedMessages ->
    pagedMessage.get()?.let { out(it) }
}
