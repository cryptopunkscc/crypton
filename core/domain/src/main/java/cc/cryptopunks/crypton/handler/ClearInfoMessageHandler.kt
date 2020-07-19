package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.ChatScope
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.handle
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

internal fun ChatScope.handleClearInfoMessages() =
    handle<Chat.Service.ClearInfoMessages> {
        GlobalScope.launch {
            messageRepo.run { delete(list(chat.address, Message.Status.Info)) }
            log.d("Info messages deleted for ${chat.address}")
        }
    }
