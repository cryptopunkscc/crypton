package cc.cryptopunks.crypton.sys

import android.net.ConnectivityManager
import android.net.NetworkRequest
import cc.cryptopunks.crypton.util.typedLog

internal fun ConnectivityManager.bind(callbackFlow: NetworkCallbackFlow) {
    try {
        registerNetworkCallback(
            NetworkRequest.Builder().build(),
            callbackFlow
        )
        allNetworks.forEach { network ->
            callbackFlow.onAvailable(network)
        }
    } catch (throwable: Throwable) {
        typedLog().e(throwable)
    }
}