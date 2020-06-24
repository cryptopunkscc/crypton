package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.context.handle
import kotlinx.coroutines.CoroutineScope

fun CoroutineScope.handleNewSession() = handle<SessionScope> {
    startSessionService(this@handle)
}
