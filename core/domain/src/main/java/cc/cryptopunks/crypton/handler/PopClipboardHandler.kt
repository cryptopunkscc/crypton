package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.handle
import cc.cryptopunks.crypton.util.pop

internal fun handlePopClipboard() = handle { out, _: Chat.Service.PopClipboard ->
    clipboardStore.pop()?.run {
        out(Chat.Service.MessageText(data))
    }
}
