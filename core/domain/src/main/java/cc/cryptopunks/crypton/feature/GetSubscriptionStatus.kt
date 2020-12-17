package cc.cryptopunks.crypton.feature

import cc.cryptopunks.crypton.cliv2.command
import cc.cryptopunks.crypton.cliv2.config
import cc.cryptopunks.crypton.cliv2.param
import cc.cryptopunks.crypton.context.Get
import cc.cryptopunks.crypton.context.address
import cc.cryptopunks.crypton.context.rosterNet
import cc.cryptopunks.crypton.feature
import cc.cryptopunks.crypton.inContext

internal fun getSubscriptionStatus() = feature(

    command = command(
        config("account"),
        param().copy(name = "local@domain"),
        name = "status of",
        description = "Get roster subscription status"
    ) { (account, buddy) ->
        Get.SubscriptionStatus(address(buddy)).inContext(account)
    },

    handler = { out, get: Get.SubscriptionStatus ->
        rosterNet.subscriptionStatus(get.address).out()
    }
)
