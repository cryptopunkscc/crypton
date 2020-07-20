package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.AppScope

internal fun AppScope.interruptSessions() {
    sessions.get().values.forEach { session ->
        session.interrupt()
    }
}
