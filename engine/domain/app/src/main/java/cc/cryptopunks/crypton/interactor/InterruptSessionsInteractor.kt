package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.AppScope

fun AppScope.interruptSessions() {
    sessionStore.get().values.forEach { session ->
        session.interrupt()
    }
}
