package cc.cryptopunks.crypton.sys

import android.net.ConnectivityManager
import cc.cryptopunks.crypton.context.Network
import cc.cryptopunks.crypton.context.Network.Status.Unavailable
import cc.cryptopunks.crypton.context.Service
import cc.cryptopunks.crypton.sys.network.NetworkCallbacks
import cc.cryptopunks.crypton.sys.network.bind
import cc.cryptopunks.crypton.sys.network.processNetworkStatus
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*

internal class NetworkSys(
    connectivityManager: ConnectivityManager,
    scope: Service.Scope
) :
    Network.Sys {

    private val channel = ConflatedBroadcastChannel<Network.Status>(Unavailable)

    init {
        val networkCallbacks = NetworkCallbacks(scope)
        scope.launch {
            networkCallbacks.flow()
                .map(processNetworkStatus)
                .filterNotNull()
                .collect(channel::send)
        }

        connectivityManager.bind(networkCallbacks)
    }

    override val status: Network.Status get() = channel.value

    override fun statusFlow(): Flow<Network.Status> = channel.asFlow()
}
