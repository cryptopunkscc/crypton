package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.handle

internal fun AppScope.handleCopy() = handle<Chat.Service.Copy> {
    clipboardSys.setClip(message.text)
}
