package cc.cryptopunks.crypton.feature

import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.account
import cc.cryptopunks.crypton.context.net
import cc.cryptopunks.crypton.create.handler
import cc.cryptopunks.crypton.feature
import cc.cryptopunks.crypton.interactor.removeSessionScope
import cc.cryptopunks.crypton.logv2.d
import kotlinx.coroutines.cancel
import java.util.concurrent.CancellationException

internal fun logout() = feature(
    handler = handler { _, arg: Exec.Disconnect ->
        removeSessionScope(account.address) {
            net.disconnect()
            cancel(CancellationException(arg.toString()))
            log.d { "Successful logout $account" }
        }
    }
)
