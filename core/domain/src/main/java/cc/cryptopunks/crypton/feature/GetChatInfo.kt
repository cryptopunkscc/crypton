package cc.cryptopunks.crypton.feature

import cc.cryptopunks.crypton.cliv2.command
import cc.cryptopunks.crypton.cliv2.config
import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Get
import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.context.chat
import cc.cryptopunks.crypton.context.chatNet
import cc.cryptopunks.crypton.context.rosterNet
import cc.cryptopunks.crypton.feature
import cc.cryptopunks.crypton.factory.handler
import cc.cryptopunks.crypton.inScope

internal fun getChatInfo() = feature(

    command = command(
        config("account"),
        config("chat"),
        name = "chat info",
        description = "Display info about chat."
    ) { (account, chat) ->
        Get.ChatInfo.inScope(account, chat)
    },

    handler = handler {out, _: Get.ChatInfo ->
        chat.run {
            when (isConference) {
                true -> chatNet.getChatInfo(address)
                false -> getRosterSubscription(address)
            }
        }.out()
    }
)

fun SessionScope.getRosterSubscription(address: Address) =
    Roster.Subscription(address, rosterNet.subscriptionStatus(address))
