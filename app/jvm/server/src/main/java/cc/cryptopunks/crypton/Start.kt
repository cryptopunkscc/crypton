package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.net.connect
import cc.cryptopunks.crypton.net.startServerSocket
import cc.cryptopunks.crypton.util.logger.CoroutineLog
import kotlinx.coroutines.withContext
import java.net.InetSocketAddress

suspend fun Server.start(): Unit =
    withContext(
        CoroutineLog.Label("CryptonServer")
    ) {
        init()
        startServerSocket(
            InetSocketAddress(
                config.socketAddress,
                config.socketPort
            )
        ).connect(service)
    }

class CliApplication(

)
