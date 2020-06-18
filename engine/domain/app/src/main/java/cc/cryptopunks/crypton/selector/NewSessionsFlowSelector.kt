package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.context.SessionScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.onEach

fun AppScope.newSessionsFlow(
): Flow<SessionScope> {
    var sessions = emptyMap<Address, SessionScope>()
    return sessionStore.changesFlow().flatMapConcat { current ->
        val new = (current - sessions.keys)
        sessions = current
        new.map { it.value }.asFlow()
    }.onEach {
        log.d("New session ${it.address}")
    }
}
