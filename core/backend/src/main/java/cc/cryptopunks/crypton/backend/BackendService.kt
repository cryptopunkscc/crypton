package cc.cryptopunks.crypton.backend

import cc.cryptopunks.crypton.Connectable
import cc.cryptopunks.crypton.Connector
import cc.cryptopunks.crypton.connectable
import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.contextDecoder
import cc.cryptopunks.crypton.service.startAppService
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
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
