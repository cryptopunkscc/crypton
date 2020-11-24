package cc.cryptopunks.crypton.feature

import cc.cryptopunks.crypton.connector
import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.RootScope
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.context.Subscribe
import cc.cryptopunks.crypton.context.inContext
import cc.cryptopunks.crypton.createEmitters
import cc.cryptopunks.crypton.emitter
import cc.cryptopunks.crypton.feature
import cc.cryptopunks.crypton.service
import cc.cryptopunks.crypton.serviceName
import cc.cryptopunks.crypton.util.Log
import cc.cryptopunks.crypton.util.logger.CoroutineLog
import cc.cryptopunks.crypton.util.logger.log
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach

internal fun startSessionService() = feature(

    emitter = emitter<RootScope> {
        mutableSetOf<Address>().let { last ->
            sessions.changesFlow().flatMapConcat { current ->
                val new = (current - last)
                last.clear()
                last += current.keys
                new.map { it.value }.asFlow()
            }.distinctUntilChanged().onEach { scope ->
                log.d { "Start services request ${scope.address}" }
            }.map { scope ->
                Subscribe.SessionService.inContext(scope.address)
            }.onCompletion {
                log.d { "Close newSessionsFlow" }
            }
        }
    },

    handler = { _, _: Subscribe.SessionService ->
        log.d { "Invoke session services for $address" }

        val context = coroutineContext
            .minusKey(Job)
            .plus(CoroutineLog.Label("SessionEmitter"))
            .plus(CoroutineLog.Action(Subscribe.SessionService))
            .plus(CoroutineLog.Status(Log.Event.Status.Handling))

        val emitters = createEmitters<SessionScope>(features)
            .flowOn(context)
            .connector()

        service("Session".serviceName)
            .run { emitters.connect() }
            .join()
    }
)
