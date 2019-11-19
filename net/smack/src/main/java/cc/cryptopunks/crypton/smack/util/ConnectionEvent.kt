package cc.cryptopunks.crypton.smack.util

import cc.cryptopunks.crypton.net.Net
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import org.jivesoftware.smack.ConnectionListener
import org.jivesoftware.smack.XMPPConnection

internal fun XMPPConnection.connectionEventsFlow() = callbackFlow<Net.Event> {
    val listener = object : ConnectionListener {
        override fun connected(connection: XMPPConnection) {
            channel.offer(Net.Event.Connected)
        }
        override fun connectionClosed() {
            channel.offer(Net.Event.ConnectionClosed())
        }
        override fun connectionClosedOnError(e: Exception) {
            channel.offer(Net.Event.ConnectionClosed(e))
        }
        override fun authenticated(connection: XMPPConnection, resumed: Boolean) {
            channel.offer(Net.Event.Authenticated(resumed))
        }
    }
    addConnectionListener(listener)
    awaitClose { removeConnectionListener(listener) }
}