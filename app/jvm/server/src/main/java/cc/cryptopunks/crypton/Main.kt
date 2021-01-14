package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.context.Subscribe
import cc.cryptopunks.crypton.service.start
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

internal fun main() {
    runBlocking {
        initJvmLog()
        TrustAllManager.install()

        val config = ServerConfig()
            .default()
//            .local()

        createServerScope(config).launch {
            launch { Subscribe.AppService.start { println(this) } }
            launch { server(config) }
        }.join()
    }
}
