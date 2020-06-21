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
import kotlinx.coroutines.launch

class BackendService(
    private val appScope: AppScope
) : Connectable {

    private val log = typedLog()

    override val coroutineContext get() = appScope.coroutineContext

    init {
        appScope.startAppService()
    }

    override fun Connector.connect(): Job = Proxy(output).run {
        log.d("Connect")
        val backend = Backend(appScope)
        launch {
            backend.routeSys.bind(backend)
            input.map {
                log.d("Received $it")
                it as? Route ?: send(it)
            }.filterIsInstance<Route>().fold(Actor.Empty) { old, route ->
                newActor().also { new -> backend.switchContext(route, old, new) }
            }
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

private fun Backend.switchContext(route: Route, old: Actor, new: Actor) {
    top()?.minus(old)
    navigate(route)
    top()?.plus(new)
}
