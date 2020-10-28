package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Get
import cc.cryptopunks.crypton.handle

internal fun handleListAccounts() = handle { out, _: Get.Accounts ->
    out(accounts.get())
}
