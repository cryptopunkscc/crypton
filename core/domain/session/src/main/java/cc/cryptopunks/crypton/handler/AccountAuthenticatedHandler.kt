import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.handle
import cc.cryptopunks.crypton.interactor.syncConferencesWithRetry

internal fun SessionScope.handleAccountAuthenticated() =
    handle<Account.Authenticated> { out ->
        syncConferencesWithRetry(out)
    }

