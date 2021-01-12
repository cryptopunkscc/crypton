package cc.cryptopunks.crypton.feature

import cc.cryptopunks.crypton.cliv2.command
import cc.cryptopunks.crypton.cliv2.config
import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.chat
import cc.cryptopunks.crypton.context.chatRepo
import cc.cryptopunks.crypton.create.handler
import cc.cryptopunks.crypton.create.inScope
import cc.cryptopunks.crypton.feature

internal fun deleteChat() = feature(

    command(
        config("account"),
        config("chat"),
        name = "delete chat",
        description = "Delete each chat form given list"
    ) { (account, chat) ->
        Exec.DeleteChat.inScope(account, chat)
    },

    handler { _, _: Exec.DeleteChat ->
        chatRepo.delete(listOf(chat.address))
    }
)
