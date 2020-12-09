package cc.cryptopunks.crypton.feature

import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.feature
import cc.cryptopunks.crypton.interactor.removeSessionScope
import cc.cryptopunks.crypton.util.logger.log
import kotlinx.coroutines.cancel
import java.util.concurrent.CancellationException

internal fun logout() = feature(
    handler = { _, arg: Exec.Disconnect ->
        removeSessionScope(account.address) {
            disconnect()
            cancel(CancellationException(arg.toString()))
            log.d { "Successful logout $account" }
        }
    }
)
