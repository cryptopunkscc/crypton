package cc.cryptopunks.crypton.sys.network

import android.net.ConnectivityManager
import cc.cryptopunks.crypton.context.Network
import cc.cryptopunks.crypton.context.Service
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow

internal class NetworkCallbacks(
    private val scope: Service.Scope
) :
    ConnectivityManager.NetworkCallback() {

    private val log = typedLog()

    private val channel = ConflatedBroadcastChannel<Network.Adapter>()

    private fun send(
        status: Network.Status,
        network: android.net.Network? = null
    ) = scope.launch {
        channel.send(
            Network.Adapter(
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

    fun flow() = channel.asFlow()
}
