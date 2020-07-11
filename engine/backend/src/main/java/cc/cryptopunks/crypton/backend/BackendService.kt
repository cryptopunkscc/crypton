package cc.cryptopunks.crypton.backend

import cc.cryptopunks.crypton.context.Actor
import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.context.Connectable
import cc.cryptopunks.crypton.context.Connector
import cc.cryptopunks.crypton.context.Route
import cc.cryptopunks.crypton.context.actor
import cc.cryptopunks.crypton.service.startAppService
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext

class BackendService(
    val appScope: AppScope
) : Connectable {

    private val log = typedLog()

    override val coroutineContext = SupervisorJob() + newSingleThreadContext("BackendService")

    init {
        appScope.startAppService()
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
                it as? Route ?: send(it)
            }.filterIsInstance<Route>().fold(Actor.Empty) { old, route ->
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

private suspend fun Backend.switchContext(route: Route, old: Actor, new: Actor) {
    top()?.minus(old)
    when (route) {
        is Route.Back -> drop()
        else -> request(route)
    }
    top()?.plus(new)
}
