package cc.cryptopunks.crypton.net

import cc.cryptopunks.crypton.decodeScopedActions
import cc.cryptopunks.crypton.logv2.d
import cc.cryptopunks.crypton.logv2.e
import cc.cryptopunks.crypton.logv2.log
import cc.cryptopunks.crypton.service.start
import cc.cryptopunks.crypton.util.ext.invokeOnClose
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.newSingleThreadContext
import java.net.InetSocketAddress
import kotlin.coroutines.CoroutineContext

suspend fun startServerSocket(
    address: InetSocketAddress,
    context: CoroutineContext = newSingleThreadContext("Server"),
): ServerSocket = coroutineScope {
    aSocket(
        selector = ActorSelectorManager(context)
    ).tcp().bind(address).apply {
        log.d { "Start server socket: $localAddress" }
    }
}

suspend fun ServerSocket.startService(): Unit =
    coroutineScope {
        invokeOnClose { e ->
            logClose(e)
            close()
        }
        flow {
            while (true) emit(accept())
        }.onEach { socket ->
            logAccepted(socket)
        }.collect { socket ->
            try {
                socket.connector()
                    .decodeScopedActions()
//                    .logging()
                    .start()
            } catch (e: Throwable) {
                e.printStackTrace()
                socket.close()
            }
        }
    }

private fun CoroutineScope.logAccepted(socket: Socket) = log.d {
    "Accepted ${socket.remoteAddress}"
}

private fun CoroutineScope.logClose(e: Throwable?) {
    log.d { "close server" }
    e?.let { log.e { it } }
}
