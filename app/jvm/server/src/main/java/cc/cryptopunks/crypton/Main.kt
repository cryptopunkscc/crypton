package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.backend.BackendService
import kotlinx.coroutines.runBlocking

internal fun main() {
    runBlocking {
        initJvmLog()
        TrustAllManager.install()

        val config = ServerConfig().default().local()
        val backend = BackendService(createServerScope(config)).init()

        server(config, backend).invoke()
    }
}
