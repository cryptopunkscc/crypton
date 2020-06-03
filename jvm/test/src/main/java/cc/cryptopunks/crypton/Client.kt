package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.util.typedLog
import io.ktor.network.selector.ActorSelectorManager
import io.ktor.network.sockets.aSocket
import kotlinx.coroutines.*
import java.net.InetSocketAddress

private class Client

suspend fun connectClient(
    host: String = "127.0.0.1",
    port: Int = 2323,
    block: suspend ClientDsl.() -> Unit
) {
    val client = Client()
    val log = client.typedLog()
    withContext(newSingleThreadContext(client.toString())) {
        aSocket(ActorSelectorManager(Dispatchers.IO)).tcp()
            .connect(InetSocketAddress(host, port))
            .connector(log)
            .apply {
                launch { ClientDsl(this@apply, log).block() }
            }
    }
}
