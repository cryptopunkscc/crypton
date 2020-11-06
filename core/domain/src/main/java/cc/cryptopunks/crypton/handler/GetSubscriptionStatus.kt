package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Get
import cc.cryptopunks.crypton.handle

fun handleGetSubscriptionStatus() = handle { out, get: Get.SubscriptionStatus ->
    subscriptionStatus(get.address).out()
}
