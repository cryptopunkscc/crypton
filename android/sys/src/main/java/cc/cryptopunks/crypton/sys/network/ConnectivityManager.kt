package cc.cryptopunks.crypton.sys.network

import android.net.ConnectivityManager
import android.net.NetworkRequest
import cc.cryptopunks.crypton.util.typedLog

internal fun ConnectivityManager.bind(callbacks: ConnectivityManager.NetworkCallback) {
    try {
        registerNetworkCallback(
            NetworkRequest.Builder().build(),
            callbacks
        )
        allNetworks.forEach { network ->
            callbacks.onAvailable(network)
        }
    } catch (throwable: Throwable) {
        typedLog().e(throwable)
    }
}
