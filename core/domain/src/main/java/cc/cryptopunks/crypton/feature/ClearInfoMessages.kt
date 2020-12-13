package cc.cryptopunks.crypton.feature

import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.chat
import cc.cryptopunks.crypton.context.messageRepo
import cc.cryptopunks.crypton.feature
import cc.cryptopunks.crypton.util.logger.log

internal fun clearInfoMessages() = feature(

    handler = { _, _: Exec.ClearInfoMessages ->
        val chatAddress = chat.address
        messageRepo.run {
            delete(list(chatAddress, Message.Type.Info))
        }
        log.d { "Info messages deleted for $chatAddress" }
    }
)
