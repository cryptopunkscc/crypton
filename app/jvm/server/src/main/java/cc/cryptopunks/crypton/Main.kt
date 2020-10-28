package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.backend.BackendService
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        initJvmLog()
        TrustAllManager.install()

        val config = Server.Config().default().local()
        val scope = config.rootScope()
        val service = BackendService(scope).init()

        Server(config, service).start()
    }
}
