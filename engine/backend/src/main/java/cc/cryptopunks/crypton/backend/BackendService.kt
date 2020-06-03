package cc.cryptopunks.crypton.backend

import cc.cryptopunks.crypton.context.*
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext

class BackendService(
    private val appCore: AppCore
) : Connectable {

    private val log = typedLog()

    override val coroutineContext =
        SupervisorJob() + newSingleThreadContext(this::class.simpleName!!)

    init {
        Backend(appCore).appService()
    }

    override fun Connector.connect(): Job = Backend(appCore).run {
        log.d("Connect")
        val proxy = Proxy(output)
        var actor = actor()
        launch {
            routeSys.bind(this@run)
            log.d("Start collecting")
            input.collect { arg ->
                log.d("Received $arg")
                if (arg is Route) {
                    top()?.minus(actor)
                    actor = proxy()
                    navigate(arg)
                    top()?.plus(actor)
                } else {
                    proxy.send(arg)
                }
            }
        }
    }
}

private class Proxy(
    private val output: suspend (Any) -> Unit
) {
    private val channel = BroadcastChannel<Any>(Channel.BUFFERED)
    val send = channel::send

    operator fun invoke(): Actor = Connector(
        input = channel.asFlow(),
        output = output
    ).actor()
}
