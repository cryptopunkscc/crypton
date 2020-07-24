package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.handle

internal fun handleCopy() = handle { _, (message): Chat.Service.Copy ->
    clipboardSys.setClip(message.text)
}
