package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.handle

internal fun handleDeleteMessage() = handle { _, (message): Exec.DeleteMessage ->
    messageRepo.delete(message)
}
