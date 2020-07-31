package cc.cryptopunks.crypton.handler

import androidx.paging.PagedList
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.ChatScope
import cc.cryptopunks.crypton.context.Get
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Subscribe
import cc.cryptopunks.crypton.handle
import cc.cryptopunks.crypton.selector.messagePagedListFlow
import cc.cryptopunks.crypton.util.logger.log
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

internal fun handlePageMessagesSubscription() =
    handle { out, _: Subscribe.PagedMessages ->
        messagePagedListFlow()
            .onEach(pagedMessagesReceived)
            .map { Chat.PagedMessages(address, it) }
            .onEach { pagedMessage { it } }
            .collect(out)
    }

private val ChatScope.pagedMessagesReceived: suspend (PagedList<Message>) -> Unit
    get() = { list -> log.d { "Received ${list.size} messages for chat $chat" } }

internal fun handleGetPagedMessages() = handle { out, _: Get.PagedMessages ->
    pagedMessage.get()?.let { out(it) }
}
