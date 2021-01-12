package cc.cryptopunks.crypton.feature

import androidx.paging.PagedList
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.ChatScope
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Subscribe
import cc.cryptopunks.crypton.context.account
import cc.cryptopunks.crypton.context.chat
import cc.cryptopunks.crypton.context.pagedMessages
import cc.cryptopunks.crypton.create.handler
import cc.cryptopunks.crypton.feature
import cc.cryptopunks.crypton.logv2.d
import cc.cryptopunks.crypton.logv2.log
import cc.cryptopunks.crypton.selector.messagePagedListFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

internal fun subscribePagedMessages() = feature(
    handler { out, _: Subscribe.PagedMessages ->
        val pagedMessages = pagedMessages
        messagePagedListFlow()
            .onEach(pagedMessagesReceived)
            .map { Chat.PagedMessages(account.address, it) }
            .onEach { pagedMessages { it } }
            .collect(out)
    }
)

private val ChatScope.pagedMessagesReceived: suspend (PagedList<Message>) -> Unit
    get() = { list ->
        val size = list.size
        log.d { "Received $size messages for chat $chat" }
    }
