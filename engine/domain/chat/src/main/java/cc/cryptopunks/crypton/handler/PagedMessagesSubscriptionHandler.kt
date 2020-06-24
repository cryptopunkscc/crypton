package cc.cryptopunks.crypton.handler

import androidx.paging.PagedList
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.ChatScope
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.handle
import cc.cryptopunks.crypton.selector.messagePagedListFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

internal fun ChatScope.handlePageMessagesSubscription() =
    handle<Chat.Service.SubscribePagedMessages> { output ->
        messagePagedListFlow()
            .onEach(pagedMessagesReceived)
            .map { Chat.Service.PagedMessages(address, it) }
            .collect(output)
    }

private val ChatScope.pagedMessagesReceived: suspend (PagedList<Message>) -> Unit
    get() = { list -> log.d("Received ${list.size} messages for chat $chat") }
