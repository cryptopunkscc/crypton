package cc.cryptopunks.crypton.emitter

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.RootScope
import cc.cryptopunks.crypton.context.context
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach

fun RootScope.startSessionServicesFlow() =
    mutableSetOf<Address>().let { sessions ->
        this.sessions.changesFlow().flatMapConcat { current ->
            val new = (current - sessions)
            sessions.clear()
            sessions += current.keys
            new.map { it.value }.asFlow()
        }.distinctUntilChanged().onEach { scope ->
            log.d("Start services request ${scope.address}")
        }.map { scope ->
            Exec.SessionService.context(scope.address)
        }.onCompletion {
            log.d("Close newSessionsFlow")
        }
    }
