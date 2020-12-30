package cc.cryptopunks.crypton.feature

import cc.cryptopunks.crypton.cliv2.command
import cc.cryptopunks.crypton.cliv2.config
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Get
import cc.cryptopunks.crypton.context.chatNet
import cc.cryptopunks.crypton.feature
import cc.cryptopunks.crypton.factory.handler
import cc.cryptopunks.crypton.inScope

internal fun getHostedRooms() = feature(

    command = command(
        config("account"),
        name = "hosted rooms",
        description = "List hosted rooms."
    ) { (account) ->
        Get.HostedRooms.inScope(account)
    },

    handler = handler {out, _: Get.HostedRooms ->
        Chat.AllRooms(chatNet.listHostedRooms().toSet()).out()
    }
)
