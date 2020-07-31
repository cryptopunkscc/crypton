package cc.cryptopunks.crypton.net

import cc.cryptopunks.crypton.Connectable
import cc.cryptopunks.crypton.util.Log
import cc.cryptopunks.crypton.util.ext.invokeOnClose
import cc.cryptopunks.crypton.util.logger.CoroutineLog
import cc.cryptopunks.crypton.util.logger.log
import io.ktor.network.selector.ActorSelectorManager
import io.ktor.network.sockets.ServerSocket
import io.ktor.network.sockets.Socket
import io.ktor.network.sockets.aSocket
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.newSingleThreadContext
import java.net.InetSocketAddress
import kotlin.coroutines.CoroutineContext

suspend fun startServerSocket(
    address: InetSocketAddress,
    context: CoroutineContext = newSingleThreadContext("Server")
): ServerSocket = coroutineScope {
    aSocket(ActorSelectorManager(context))
        .tcp()
        .bind(address)
        .apply {
            log.builder.d {
                message = localAddress.toString()
                status = Log.Event.Status.Start.name
            }
        }
}


suspend fun ServerSocket.connect(
    connectable: Connectable
) = coroutineScope {
    val context = coroutineContext
    invokeOnClose { e ->
        context.log.builder.d {
            message = "close server"
            throwable = e
            status = Log.Event.Status.Finished.name
        }
        connectable.cancel("Server close", e)
        close()
    }
    flow {
        while (true) emit(accept())
    }.onEach { socket ->
        log.builder.d {
            message = socket.remoteAddress.toString()
            status = "Accepted"
        }
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
