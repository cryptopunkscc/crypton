package cc.cryptopunks.crypton.feature

import cc.cryptopunks.crypton.cliv2.command
import cc.cryptopunks.crypton.cliv2.config
import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.chat
import cc.cryptopunks.crypton.context.chatNet
import cc.cryptopunks.crypton.create.handler
import cc.cryptopunks.crypton.create.inScope
import cc.cryptopunks.crypton.feature


internal fun configureChat() = feature(

    command(
        config("account"),
        config("chat"),
        name = "configure conference",
        description = "Configure conference (WIP)."
    ) { (account, chat) ->
        Exec.ConfigureConference().inScope(account, chat)
    },

    handler { _, _: Exec.ConfigureConference ->
        val chat = chat
        if (chat.isConference)
            chatNet.configureConference(chat.address)
    }
)
