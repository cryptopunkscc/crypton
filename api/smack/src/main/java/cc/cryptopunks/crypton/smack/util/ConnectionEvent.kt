package cc.cryptopunks.crypton.smack.util

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import org.jivesoftware.smack.ConnectionListener
import org.jivesoftware.smack.XMPPConnection

internal sealed class ConnectionEvent {
    object Connected : ConnectionEvent()
    data class ConnectionClosed(val throwable: Throwable? = null) : ConnectionEvent() {
        val withError get() = throwable != null
    }
    data class Authenticated(val resumed: Boolean) : ConnectionEvent()
}

internal fun XMPPConnection.connectionEventsFlow() = callbackFlow<ConnectionEvent> {
    val listener = object : ConnectionListener {
        override fun connected(connection: XMPPConnection) {
            channel.offer(ConnectionEvent.Connected)
        }
        override fun connectionClosed() {
            channel.offer(ConnectionEvent.ConnectionClosed())
        }
        override fun connectionClosedOnError(e: Exception) {
            channel.offer(ConnectionEvent.ConnectionClosed(e))
        }
        override fun authenticated(connection: XMPPConnection, resumed: Boolean) {
            channel.offer(ConnectionEvent.Authenticated(resumed))
        }
    }
    addConnectionListener(listener)
    awaitClose { removeConnectionListener(listener) }
}