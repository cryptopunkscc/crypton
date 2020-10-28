package cc.cryptopunks.crypton.backend

import cc.cryptopunks.crypton.Connectable
import cc.cryptopunks.crypton.Connector
import cc.cryptopunks.crypton.context.RootScope
import cc.cryptopunks.crypton.contextDecoder
import cc.cryptopunks.crypton.service
import cc.cryptopunks.crypton.service.startAppService
import cc.cryptopunks.crypton.serviceName
import cc.cryptopunks.crypton.util.logger.CoroutineLog
import cc.cryptopunks.crypton.util.logger.log
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext

class BackendService(
    private val scope: RootScope
) : Connectable {

    private val job = SupervisorJob()

    override val coroutineContext = job +
        newSingleThreadContext("BackendService") +
        CoroutineLog.Label(javaClass.simpleName)

    private val lazyInit by lazy {
        scope.startAppService()
        job.invokeOnCompletion {
            scope.cancel("Close BackendService", it)
        }
    }

    fun init() = apply {
        lazyInit
    }

    override fun Connector.connect(): Job = launch {
        lazyInit
        log.builder.d { status = "Connect" }
        val decode = contextDecoder()
        scope.service(this@BackendService.serviceName).run {
            copy(
                input = input.mapNotNull(decode)
            ).connect()
        }.join()
        log.builder.d { status = "Disconnect" }
    }
}
