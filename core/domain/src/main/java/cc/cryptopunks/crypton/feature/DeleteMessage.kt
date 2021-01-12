package cc.cryptopunks.crypton.feature

import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.messageRepo
import cc.cryptopunks.crypton.create.handler
import cc.cryptopunks.crypton.feature

internal fun deleteMessage() = feature(

    handler { _, (message): Exec.DeleteMessage ->
        messageRepo.delete(message)
    }
)
