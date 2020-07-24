import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.handle
import cc.cryptopunks.crypton.interactor.syncConferencesWithRetry

internal fun handleSyncConferences() =
    handle { out, _: Exec.SyncConferences ->
        syncConferencesWithRetry(out)
    }

