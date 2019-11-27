package cc.cryptopunks.crypton.sys

import android.net.ConnectivityManager
import cc.cryptopunks.crypton.context.Network
import cc.cryptopunks.crypton.context.Service
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.asFlow
import javax.inject.Inject

class NetworkCallbackFlow @Inject constructor(
    private val scope: Service.Scope
) :
    Flow<NetworkStatus>,
    ConnectivityManager.NetworkCallback() {

    private val log = typedLog()

    private val channel = ConflatedBroadcastChannel<NetworkStatus>()

    private fun send(
        status: Network.Status,
        network: android.net.Network? = null
    ) = scope.launch {
        channel.send(
            NetworkStatus(
                status = status,
                network = network?.toString()
            )
        )
    }

    override fun onAvailable(network: android.net.Network) {
        log.d("available: $network")
        send(Network.Status.Available, network)
    }

    override fun onLost(network: android.net.Network) {
        log.d("lost: $network")
        send(Network.Status.Lost, network)
    }

    override fun onUnavailable() {
        log.d("unavailable")
        send(Network.Status.Unavailable)
    }

    @InternalCoroutinesApi
    override suspend fun collect(collector: FlowCollector<NetworkStatus>) {
        channel.asFlow().collect(collector)
    }
}