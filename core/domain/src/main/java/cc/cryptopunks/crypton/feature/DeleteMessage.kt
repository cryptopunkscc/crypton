package cc.cryptopunks.crypton.feature

import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.messageRepo
import cc.cryptopunks.crypton.factory.handler
import cc.cryptopunks.crypton.feature

internal fun deleteMessage() = feature(

    handler = handler { _, (message): Exec.DeleteMessage ->
        messageRepo.delete(message)
    }
)
