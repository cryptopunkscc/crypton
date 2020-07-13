package cc.cryptopunks.crypton.net

import cc.cryptopunks.crypton.Connectable
import cc.cryptopunks.crypton.util.TypedLog
import io.ktor.network.selector.ActorSelectorManager
import io.ktor.network.sockets.ServerSocket
import io.ktor.network.sockets.Socket
import io.ktor.network.sockets.aSocket
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.newSingleThreadContext
import java.net.InetSocketAddress
import kotlin.coroutines.CoroutineContext

fun startServerSocket(
    address: InetSocketAddress,
    log: TypedLog,
    context: CoroutineContext = newSingleThreadContext("Server")
): ServerSocket =
    aSocket(ActorSelectorManager(context))
        .tcp()
        .bind(address)
        .apply { log.d("Started at $localAddress") }


suspend fun ServerSocket.connect(
    log: TypedLog,
    connectable: Connectable
) = coroutineScope {
    flow {
        while (true) emit(accept())
    }.onCompletion { throwable ->
        log.d("close server $throwable")
        connectable.cancel("Server close", throwable)
        close()
    }.onEach { socket ->
        log.d("Socket accepted: ${socket.remoteAddress}")
    }.collect { socket ->
        connectable.tryConnectTo(socket, log)
    }
}

private fun Connectable.tryConnectTo(socket: Socket, log: TypedLog) = let {
    try {
        socket.connector(log).connect().apply {
            invokeOnCompletion { socket.close() }
        }
    } catch (e: Throwable) {
        e.printStackTrace()
        socket.close()
    }
}
