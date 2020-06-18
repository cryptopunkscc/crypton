package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.handle
import cc.cryptopunks.crypton.util.pop
import kotlinx.coroutines.launch

internal fun AppScope.handlePopClipboard() =
    handle<Chat.Service.PopClipboard> { out ->
        launch {
            clipboardStore.pop()?.run {
                out(Chat.Service.MessageText(data))
            }
        }
    }
