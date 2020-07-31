package cc.cryptopunks.crypton.net

import cc.cryptopunks.crypton.Connectable
import cc.cryptopunks.crypton.util.ext.invokeOnClose
import cc.cryptopunks.crypton.util.logger.CoroutineLog
import cc.cryptopunks.crypton.util.logger.TypedLog
import cc.cryptopunks.crypton.util.logger.coroutineLog
import cc.cryptopunks.crypton.util.logger.log
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
        .apply { log.d { "Started at $localAddress" } }


suspend fun ServerSocket.connect(
    connectable: Connectable
) = coroutineScope {
    val _log = coroutineLog()
    invokeOnClose { throwable ->
        _log.d { "close server $throwable" }
        connectable.cancel("Server close", throwable)
        close()
    }
    flow {
        while (true) emit(accept())
    }.onCompletion { throwable ->
        log.d { "close server $throwable" }
        connectable.cancel("Server close", throwable)
        close()
    }.onEach { socket ->
        log.d { "Socket accepted: ${socket.remoteAddress}" }
    }.collect { socket ->
        connectable.tryConnectTo(socket, log)
    }

}

private fun Connectable.tryConnectTo(socket: Socket, log: CoroutineLog) = let {
    try {
        socket.connector().connect().apply {
            invokeOnCompletion { socket.close() }
        }
    } catch (e: Throwable) {
        e.printStackTrace()
        socket.close()
    }
}
