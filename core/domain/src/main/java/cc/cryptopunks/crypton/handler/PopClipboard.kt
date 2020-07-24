package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.handle
import cc.cryptopunks.crypton.util.pop

internal fun handlePopClipboard() = handle { out, _: Exec.PopClipboard ->
    clipboardStore.pop()?.run {
        out(Chat.MessageText(data))
    }
}
