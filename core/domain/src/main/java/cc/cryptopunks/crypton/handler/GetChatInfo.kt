package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Get
import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.handle

internal fun handleGetChatInfo() = handle { out, _: Get.ChatInfo ->
    chat.run {
        when(isConference) {
            true -> getChatInfo(address)
            false -> getRosterSubscription(address)
        }
    }.out()
}

fun SessionScope.getRosterSubscription(address: Address) =
    Roster.Subscription(address, subscriptionStatus(address))
