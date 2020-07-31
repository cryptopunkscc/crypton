package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.net.connectClientSocket
import cc.cryptopunks.crypton.net.connector
import cc.cryptopunks.crypton.util.Log
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.newSingleThreadContext
import java.net.InetSocketAddress

suspend fun Any.connectDslClient(
    host: String = "127.0.0.1",
    port: Int = 2323,
    block: suspend ClientDsl.() -> Unit
) {
    val name = javaClass.simpleName
    connectClientSocket(
        InetSocketAddress(host, port),
        newSingleThreadContext(name)
    ).connector().logging().also { connector ->
        ClientDsl(name, connector).apply {
            log.builder.d { status = Log.Event.Status.Start.name }
            block()
            log.builder.d { status = Log.Event.Status.Finished.name }
            if (isActive) {
                cancel()
                delay(200)
            }
        }
        connector.close()
    }
}
