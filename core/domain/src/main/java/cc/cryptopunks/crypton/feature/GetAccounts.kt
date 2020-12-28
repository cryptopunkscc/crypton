package cc.cryptopunks.crypton.feature

import cc.cryptopunks.crypton.context.Get
import cc.cryptopunks.crypton.context.accounts
import cc.cryptopunks.crypton.feature
import cc.cryptopunks.crypton.factory.handler
internal fun getAccounts() = feature(
    handler = handler {out, _: Get.Accounts ->
        out(accounts.get())
    }
)
