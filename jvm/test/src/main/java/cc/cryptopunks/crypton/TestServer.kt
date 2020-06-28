package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking

open class TestServer {
    private val scope = CoroutineScope(
        SupervisorJob() + newSingleThreadContext(
            "server"
        )
    )
    private lateinit var serverJob: Job

    init {
        Log.init(JvmLog)
        TrustAllManager.install()
    }

    fun start() = runBlocking{
        serverJob = scope.launch {
            startServer()
        }
        delay(1000)
    }

    fun stop() {
        serverJob.cancel()
    }
}
