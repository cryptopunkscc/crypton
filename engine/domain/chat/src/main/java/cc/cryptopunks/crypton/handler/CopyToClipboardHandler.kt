package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Clip
import cc.cryptopunks.crypton.context.Session
import cc.cryptopunks.crypton.context.handle
import kotlinx.coroutines.launch

internal fun Session.handleCopy(
    clipboardSys: Clip.Board.Sys
) = handle<Chat.Service.Copy> {
    scope.launch { clipboardSys.setClip(message.text) }
}
