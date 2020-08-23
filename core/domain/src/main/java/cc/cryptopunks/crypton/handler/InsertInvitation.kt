package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.handle
import cc.cryptopunks.crypton.interactor.insertChat

internal fun handleInsertInvitation() = handle { _, arg: Exec.InsertInvitation ->
    if (!chatRepo.contains(arg.address)) {
        insertChat(
            Chat(
                address = arg.address,
                account = address
            )
        )
    }
}
