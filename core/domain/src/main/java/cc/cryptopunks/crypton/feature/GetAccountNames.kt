package cc.cryptopunks.crypton.feature

import cc.cryptopunks.crypton.cliv2.command
import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.Get
import cc.cryptopunks.crypton.context.accountRepo
import cc.cryptopunks.crypton.create.handler
import cc.cryptopunks.crypton.feature

internal fun getAccountNames() = feature(

    command(
        name = "get accounts",
        description = "List all locally added accounts."
    ) {
        Get.AccountNames
    },

    handler { out, _: Get.AccountNames ->
        Account.Many(accountRepo.addressList().toSet()).out()
    }
)
