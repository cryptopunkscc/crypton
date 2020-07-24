package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.RootScope
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.context.context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
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
        log.d("New session ${it.address} $it")
    }.onCompletion {
        log.d("Close newSessionsFlow")
    }
}

fun RootScope.startSessionServicesFlow(
) = mutableSetOf<Address>().let { sessions ->
    this.sessions.changesFlow().flatMapConcat { current ->
        val new = (current - sessions)
        sessions.clear()
        sessions += current.keys
        new.map { it.value }.asFlow()
    }.distinctUntilChanged().onEach { scope ->
        log.d("Start services request ${scope.address}")
    }.map { scope ->
        Account.Service.StartServices.context(scope.address)
    }.onCompletion {
        log.d("Close newSessionsFlow")
    }
}
