package cc.cryptopunks.crypton.handler

import androidx.paging.PagedList
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.ChatScope
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.handle
import cc.cryptopunks.crypton.selector.messagePagedListFlow
import cc.cryptopunks.crypton.util.Store
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

internal fun ChatScope.handlePageMessagesSubscription(
    lastPagedMessages: Store<Chat.Service.PagedMessages?>
) =
    handle<Chat.Service.SubscribePagedMessages> { output ->
        messagePagedListFlow()
            .onEach(pagedMessagesReceived)
            .map { Chat.Service.PagedMessages(address, it) }
            .onEach { lastPagedMessages { it } }
            .collect(output)
    }

private val ChatScope.pagedMessagesReceived: suspend (PagedList<Message>) -> Unit
    get() = { list -> log.d("Received ${list.size} messages for chat $chat") }

internal fun ChatScope.handleGetPagedMessages(
    lastPagedMessages: Store<Chat.Service.PagedMessages?>
) =
    handle<Chat.Service.GetPagedMessages> { out ->
        lastPagedMessages.get()?.let { out(it) }
    }
