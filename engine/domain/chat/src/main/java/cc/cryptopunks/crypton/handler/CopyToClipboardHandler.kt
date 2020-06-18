package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.handle
import kotlinx.coroutines.launch

internal fun AppScope.handleCopy() =
    handle<Chat.Service.Copy> {
        launch {
            clipboardSys.setClip(message.text)
        }
    }
