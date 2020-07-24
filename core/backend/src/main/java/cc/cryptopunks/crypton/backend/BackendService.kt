package cc.cryptopunks.crypton.backend

import cc.cryptopunks.crypton.Actor
import cc.cryptopunks.crypton.Connectable
import cc.cryptopunks.crypton.Connector
import cc.cryptopunks.crypton.actor
import cc.cryptopunks.crypton.connectable
import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.context.Route
import cc.cryptopunks.crypton.contextDecoder
import cc.cryptopunks.crypton.service.startAppService
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext

class BackendService(
    private val scope: AppScope
) : Connectable {

    private val log = typedLog()

    private val job = SupervisorJob()

    override val coroutineContext = job + newSingleThreadContext("BackendService")

    private val lazyInit by lazy {
        scope.startAppService()
        job.invokeOnCompletion {
            scope.cancel("Close BackendService", it)
        }
    }

    override fun Connector.connect(): Job = launch {
        lazyInit
        log.d("Connect")
        val decode = contextDecoder()
        scope.connectable().run {
            copy(
                input = input.onEach {
                    log.d("Received $it")
                }.mapNotNull(decode).onEach {
                    log.d("Decoded $it")
                }
            ).connect()
        }.join()
        log.d("Disconnect")
    }
}

class BackendService2(
    val appScope: AppScope
) : Connectable {

    private val log = typedLog()

    private val job = SupervisorJob()

    override val coroutineContext = SupervisorJob() + newSingleThreadContext("BackendService")

    init {
        appScope.startAppService()
        job.invokeOnCompletion {
            appScope.cancel("Close BackendService", it)
        }
    }

    override fun Connector.connect(): Job = Proxy(output).run {
        log.d("Connect")
        val backend = Backend(appScope)
        launch {
            input.onEach {
                log.d("Received $it")
            }.onStart {
                emit(Route.Main)
            }.map {
                it as? Route<*> ?: send(it)
            }.filterIsInstance<Route<*>>().fold(Actor.Empty) { old, route ->
                newActor().also { new -> backend.switchContext(route, old, new) }
            }
            backend.drop()
            log.d("Disconnect")
        }
    }
}

private class Proxy(
    private val output: suspend (Any) -> Unit
) {
    private val channel = BroadcastChannel<Any>(Channel.BUFFERED)
    val send: suspend (Any) -> Unit = channel::send

    fun newActor(): Actor = Connector(
        input = channel.asFlow(),
        output = output
    ).actor()
}

private suspend fun Backend.switchContext(route: Route<*>, old: Actor, new: Actor) {
    top()?.minus(old)
    when (route) {
        is Route.Back -> drop()
        else -> request(route)
    }
    top()?.plus(new)
}
