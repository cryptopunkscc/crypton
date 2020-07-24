package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.handle

internal fun handleDeleteChat() = handle { _, _: Exec.DeleteChat ->
    chatRepo.delete(listOf(chat.address))
}
