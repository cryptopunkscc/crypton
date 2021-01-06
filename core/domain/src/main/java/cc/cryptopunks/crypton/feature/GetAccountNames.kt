package cc.cryptopunks.crypton.feature

import cc.cryptopunks.crypton.cliv2.command
import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.Get
import cc.cryptopunks.crypton.context.accountRepo
import cc.cryptopunks.crypton.factory.handler
import cc.cryptopunks.crypton.feature

internal fun getAccountNames() = feature(

    command = command(
        name = "get accounts",
        description = "List all locally added accounts."
    ) {
        Get.AccountNames
    },

    handler = handler { out, _: Get.AccountNames ->
        Account.Many(accountRepo.addressList().toSet()).out()
    }
)
