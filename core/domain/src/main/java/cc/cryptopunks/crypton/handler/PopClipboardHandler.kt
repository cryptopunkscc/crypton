package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.handle
import cc.cryptopunks.crypton.util.pop

internal fun AppScope.handlePopClipboard() =
    handle<Chat.Service.PopClipboard> { out ->
        clipboardStore.pop()?.run {
            out(Chat.Service.MessageText(data))
        }
    }
