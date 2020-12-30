package cc.cryptopunks.crypton.net

import cc.cryptopunks.crypton.decodeScopedActions
import cc.cryptopunks.crypton.service.start
import cc.cryptopunks.crypton.util.Log
import cc.cryptopunks.crypton.util.ext.invokeOnClose
import cc.cryptopunks.crypton.util.logger.log
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
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
        log.builder.d {
            message = localAddress.toString()
            status = Log.Event.Status.Start.name
        }
    }
}

suspend fun ServerSocket.startService(): Unit =
    coroutineScope {
        val context = coroutineContext
        invokeOnClose { e ->
            context.logClose(e)
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

private suspend fun logAccepted(socket: Socket) = log.builder.d {
    message = socket.remoteAddress.toString()
    status = "Accepted"
}

private fun CoroutineContext.logClose(e: Throwable?) = log.builder.d {
    message = "close server"
    throwable = e
    status = Log.Event.Status.Finished.name
}
