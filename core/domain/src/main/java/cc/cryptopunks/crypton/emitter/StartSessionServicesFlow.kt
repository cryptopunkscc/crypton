package cc.cryptopunks.crypton.emitter

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.RootScope
import cc.cryptopunks.crypton.context.inContext
import cc.cryptopunks.crypton.util.logger.log
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach

fun RootScope.startSessionServicesFlow() =
    mutableSetOf<Address>().let { last ->
        sessions.changesFlow().flatMapConcat { current ->
            val new = (current - last)
            last.clear()
            last += current.keys
            new.map { it.value }.asFlow()
        }.distinctUntilChanged().onEach { scope ->
            log.d { "Start services request ${scope.address}" }
        }.map { scope ->
            Exec.SessionService.inContext(scope.address)
        }.onCompletion {
            log.d { "Close newSessionsFlow" }
        }
    }
