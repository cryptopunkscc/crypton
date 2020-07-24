package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.insertChat
import cc.cryptopunks.crypton.handle

internal fun handleInsertInvitation() = handle { _, arg: Exec.InsertInvitation ->
    log.d("handle $arg")
    if (!chatRepo.contains(arg.address)) {
        insertChat(
            Chat(
                address = arg.address,
                account = address
            )
        )
    }
}
