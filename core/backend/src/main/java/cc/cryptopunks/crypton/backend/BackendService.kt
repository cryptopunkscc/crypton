package cc.cryptopunks.crypton.backend

import cc.cryptopunks.crypton.Connectable
import cc.cryptopunks.crypton.Connector
import cc.cryptopunks.crypton.Service
import cc.cryptopunks.crypton.context.Subscribe
import cc.cryptopunks.crypton.factory.connector
import cc.cryptopunks.crypton.scopedActionDecoder
import cc.cryptopunks.crypton.service.start
import cc.cryptopunks.crypton.util.logger.CoroutineLog
import cc.cryptopunks.crypton.util.logger.log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext

class BackendService(
    val scope: CoroutineScope,
) : Connectable {

    private val job = SupervisorJob().apply {
        invokeOnCompletion { scope.cancel("Close BackendService", it) }
    }

    override val coroutineContext = scope.coroutineContext +
        job +
        newSingleThreadContext("BackendService") +
        CoroutineLog.Label(javaClass.simpleName)

    private val ensureInitialization by lazy {
        launch {
            Subscribe.AppService.start { println(this) }
            println("finish lazy AppService")
        }
    }

    fun init() = apply {
        ensureInitialization
    }

    override fun Connector.connect(): Job = launch {
        ensureInitialization
        log.builder.d { status = "Connect" }
        input.mapNotNull(scopedActionDecoder()).start { println(this) }
        log.builder.d { status = "Disconnect" }
    }

    fun connector() =
        Service().run {
            launch { start() }
            connector()
        }
}
