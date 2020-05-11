package cc.cryptopunks.crypton.backend

import cc.cryptopunks.crypton.context.*
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.scanReduce
import kotlinx.coroutines.launch

class BackendService(
    private val appCore: AppCore
) : Connectable {

    private val log = typedLog()
    override val coroutineContext = SupervisorJob() + Dispatchers.Unconfined

    init {
        Backend(appCore).appService()
    }

    override fun Connector.connect(): Job = launch {
        log.d("Connect")
        val actor = actor()
        Backend(appCore).run {
            routeSys.bind(this)
            launch {
                log.d("Start collecting")
                input.collect { arg ->
                    log.d("Received $arg")
                    if (arg is Route) navigate(arg)
                }
            }
            launch {
                bindingFlow().onEach { currentBinding ->
                    currentBinding + actor
                }.scanReduce { previousBinding, currentBinding ->
                    previousBinding - actor
                    currentBinding
                }.collect()
            }
        }
    }
}
