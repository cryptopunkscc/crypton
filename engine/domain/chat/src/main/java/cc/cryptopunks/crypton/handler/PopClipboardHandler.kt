package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Clip
import cc.cryptopunks.crypton.context.Session
import cc.cryptopunks.crypton.context.handle
import cc.cryptopunks.crypton.util.pop
import kotlinx.coroutines.launch

internal fun Session.handlePopClipboard(
    store: Clip.Board.Store
) = handle<Chat.Service.PopClipboard> { out ->
    scope.launch {
        store.pop()?.run {
            out(Chat.Service.MessageText(data))
        }
    }
}
