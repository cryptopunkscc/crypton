package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.net.connector
import io.ktor.network.selector.ActorSelectorManager
import io.ktor.network.sockets.aSocket
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.withContext
import java.net.InetSocketAddress
import kotlin.coroutines.EmptyCoroutineContext

suspend fun Any.connectClient(
    host: String = "127.0.0.1",
    port: Int = 2323,
    block: suspend ClientDsl.() -> Unit
) {
    withContext(newSingleThreadContext(toString())) {
        aSocket(ActorSelectorManager(newSingleThreadContext(toString()))).tcp()
            .connect(InetSocketAddress(host, port))
            .connector()
            .also { connector ->
                ClientDsl(connector).apply {
                    block()
                    if (isActive) {
                        cancel()
                        delay(200)
                    }
                }
                connector.close()
            }
    }
}
