package cc.cryptopunks.crypton.feature

import cc.cryptopunks.crypton.cliv2.command
import cc.cryptopunks.crypton.cliv2.config
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Get
import cc.cryptopunks.crypton.context.chatNet
import cc.cryptopunks.crypton.feature
import cc.cryptopunks.crypton.factory.handler
import cc.cryptopunks.crypton.inScope

internal fun getJoinedRooms() = feature(

    command = command(
        config("account"),
        name = "joined rooms",
        description = "List joined rooms."
    ) { (account) ->
        Get.JoinedRooms.inScope(account)
    },

    handler = handler {out, _: Get.JoinedRooms ->
        Chat.JoinedRooms(chatNet.listJoinedRooms()).out()
    }
)
