package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.RootScope

internal fun RootScope.interruptSessions() {
    sessions.get().values.forEach { session ->
        session.interrupt()
    }
}
