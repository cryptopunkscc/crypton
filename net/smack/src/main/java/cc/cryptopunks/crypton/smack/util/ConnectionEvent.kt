package cc.cryptopunks.crypton.smack.util

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.Api
import cc.cryptopunks.crypton.context.Net
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import org.jivesoftware.smack.ConnectionListener
import org.jivesoftware.smack.XMPPConnection

internal fun XMPPConnection.connectionEventsFlow() = callbackFlow<Api.Event> {
    val listener = object : ConnectionListener {
        override fun connected(connection: XMPPConnection) {
            channel.offer(Net.Event.Connected)
        }
        override fun connectionClosed() {
            channel.offer(Net.Event.Disconnected())
        }
        override fun connectionClosedOnError(e: Exception) {
            channel.offer(Net.Event.Disconnected(e))
        }
        override fun authenticated(connection: XMPPConnection, resumed: Boolean) {
            channel.offer(Account.Event.Authenticated(resumed))
        }
    }
    addConnectionListener(listener)
    awaitClose { removeConnectionListener(listener) }
}