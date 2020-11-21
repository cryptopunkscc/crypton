package cc.cryptopunks.crypton.sys

import android.net.ConnectivityManager
import android.net.NetworkRequest
import cc.cryptopunks.crypton.context.Network
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.broadcastIn
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onEach

internal class AndroidNetworkSysV2(
    connectivity: ConnectivityManager,
    scope: CoroutineScope
) : Network.Sys {

    private val channel = connectivity.networkStatusFlow().onEach { status = it }.broadcastIn(scope)

    override var status: Network.Status = Network.Status.Unavailable

    override fun statusFlow(): Flow<Network.Status> = channel.asFlow()
}

private fun ConnectivityManager.networkStatusFlow(): Flow<Network.Status> =
    callbackFlow {
        send(
            if (allNetworks.isNotEmpty())
                Network.Status.Available else
                Network.Status.Unavailable
        )
        val callback = object : ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: android.net.Network) {
                offer(Network.Status.Available)
            }

            override fun onUnavailable() {
                offer(Network.Status.Unavailable)
            }
        }
        registerNetworkCallback(NetworkRequest.Builder().build(), callback)
        awaitClose { unregisterNetworkCallback(callback) }
    }
