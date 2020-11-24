package cc.cryptopunks.crypton.feature

import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.feature

internal fun deleteMessage() = feature(

    handler = { _, (message): Exec.DeleteMessage ->
        messageRepo.delete(message)
    }
)
