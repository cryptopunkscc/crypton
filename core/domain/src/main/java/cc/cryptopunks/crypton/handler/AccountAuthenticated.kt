import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.handle
import cc.cryptopunks.crypton.interactor.syncConferencesWithRetry

internal fun handleAccountAuthenticated() = handle { out, _: Account.Authenticated ->
    syncConferencesWithRetry(out)
}

