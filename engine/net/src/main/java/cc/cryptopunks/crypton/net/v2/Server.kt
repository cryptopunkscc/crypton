package cc.cryptopunks.crypton.net.v2

import cc.cryptopunks.crypton.logv2.d
import cc.cryptopunks.crypton.logv2.log
import cc.cryptopunks.crypton.net.connector
import cc.cryptopunks.crypton.serial.decodeScopedActions
import cc.cryptopunks.crypton.service.start
import cc.cryptopunks.crypton.util.ext.invokeOnClose
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.newSingleThreadContext
import java.net.InetSocketAddress
import kotlin.coroutines.CoroutineContext

fun startServerSocket(
    address: InetSocketAddress,
    context: CoroutineContext = newSingleThreadContext("Server"),
): ServerSocket =
    aSocket(ActorSelectorManager(context)).tcp().bind(address)


suspend fun ServerSocket.startService(): Unit = coroutineScope {
    invokeOnClose { e -> close() }
    flow {
        while (true) emit(accept())
    }.collect { socket ->
        try {
            socket.connector()
        } catch (e: Throwable) {
            e.printStackTrace()
            socket.close()
        }
    }
}

private fun CoroutineScope.logAccepted(socket: Socket) = log.d {
    "Accepted ${socket.remoteAddress}"
}
