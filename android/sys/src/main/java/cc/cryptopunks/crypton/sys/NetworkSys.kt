package cc.cryptopunks.crypton.sys

import android.net.ConnectivityManager
import cc.cryptopunks.crypton.context.Network
import cc.cryptopunks.crypton.context.Network.Status.Unavailable
import cc.cryptopunks.crypton.sys.network.NetworkCallbacks
import cc.cryptopunks.crypton.sys.network.bind
import cc.cryptopunks.crypton.sys.network.processNetworkStatus
import cc.cryptopunks.crypton.util.logger.typedLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

internal class NetworkSys(
    connectivityManager: ConnectivityManager,
    scope: CoroutineScope
) :
    Network.Sys {

    private val channel = ConflatedBroadcastChannel<Network.Status>(Unavailable)

    private val log = typedLog()

    init {
        val networkCallbacks = NetworkCallbacks(scope)
        scope.launch {
            networkCallbacks.flow()
                .map(processNetworkStatus)
                .filterNotNull()
                .collect(channel::send)
        }

        connectivityManager.bind(log, networkCallbacks)
    }

    override val status: Network.Status get() = channel.value

    override fun statusFlow(): Flow<Network.Status> = channel.asFlow()
}
