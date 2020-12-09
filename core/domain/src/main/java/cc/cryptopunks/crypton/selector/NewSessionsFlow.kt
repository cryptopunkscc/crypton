package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.RootScope
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.util.logger.log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach

fun RootScope.newSessionsFlow(
): Flow<SessionScope> {
    var sessions = emptyMap<Address, SessionScope>()
    return this.sessions.changesFlow().flatMapConcat { current ->
        val new = (current - sessions.keys)
        sessions = current
        new.map { it.value }.asFlow()
    }.distinctUntilChanged().onEach {
        log.d { "New session ${it.account.address} $it" }
    }.onCompletion {
        log.d { "Close newSessionsFlow" }
    }
}
