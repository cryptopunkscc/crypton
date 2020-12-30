package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.backend.BackendService
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
            val config = ServerConfig().default().local().apply {
                profile = "test_server"
            }
            val backend = BackendService(createServerScope(config)).init()

            server(config)
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
