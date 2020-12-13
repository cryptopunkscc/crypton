package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.RootScope
import cc.cryptopunks.crypton.context.net
import cc.cryptopunks.crypton.context.sessions

internal fun RootScope.interruptSessions() {
    sessions.get().values.forEach { session ->
        session.net.interrupt()
    }
}
