package cc.cryptopunks.crypton

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking

open class TestServer {
    private val scope = CoroutineScope(
        SupervisorJob() + newSingleThreadContext("server")
    )
    private lateinit var serverJob: Job

    fun start() = runBlocking {
        serverJob = scope.launch {
            startCryptonServer()
        }
        delay(1000)
    }

    fun stop() {
        serverJob.cancel()
    }

    companion object {
        init {
            GlobalScope.launch { initJvmLog() }
            TrustAllManager.install()
        }
    }
}
