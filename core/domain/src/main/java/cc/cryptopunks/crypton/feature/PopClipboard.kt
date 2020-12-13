package cc.cryptopunks.crypton.feature

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.clipboardStore
import cc.cryptopunks.crypton.feature
import cc.cryptopunks.crypton.util.pop

internal fun popClipboard() = feature(
    handler = { out, _: Exec.PopClipboard ->
        clipboardStore.pop()?.run {
            out(Chat.MessageText(data))
        }
    }
)
