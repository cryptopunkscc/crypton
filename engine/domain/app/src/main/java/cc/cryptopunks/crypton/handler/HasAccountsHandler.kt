package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.context.handle
import kotlinx.coroutines.launch

fun AppScope.handleHasAccounts() = handle<Account.Service.HasAccounts> {
    launch {
        when (condition) {
            true -> indicatorSys.showIndicator().let { "Show" }
            false -> indicatorSys.hideIndicator().let { "Hide" }
        }.let {
            log.d("$it indicator")
        }
    }
}
