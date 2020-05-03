package cc.cryptopunks.crypton.sys.network

import cc.cryptopunks.crypton.context.Network
import cc.cryptopunks.crypton.context.Network.Status
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.delay

object NetworkStatusProcessor

private val log = NetworkStatusProcessor.typedLog()

val processNetworkStatus: suspend (Network.Adapter) -> Status? = { (network, status) ->
    when (status) {
        is Status.Available -> onAvailable(network!!)
        is Status.Lost -> onLost(network!!)
        is Status.Unavailable -> onUnavailable()
        else -> null
    }
}

private val networks = linkedSetOf<String>()

private suspend fun onAvailable(network: String) = withLog {
    val changed = networks.isNotEmpty()
    networks += network
    if (changed)
        Status.Changed else
        Status.Available
}

private suspend fun onLost(network: String) = withLog {
    val last = networks.last()
    networks -= network
    delay(200)
    when {
        networks.isEmpty() -> Status.Unavailable
        last == network -> Status.Changed
        else -> null
    }
}

private suspend fun onUnavailable() = withLog {
    if (networks.isNotEmpty()) {
        networks.clear()
    }
    Status.Unavailable
}

private suspend fun withLog(block: suspend () -> Status?): Status? = block().also {
    log.d("networks: $networks")
    log.d("out: $it")
}
