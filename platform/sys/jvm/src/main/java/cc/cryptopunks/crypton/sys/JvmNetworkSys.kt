package cc.cryptopunks.crypton.sys

import cc.cryptopunks.crypton.context.Network
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.broadcastIn
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.net.NetworkInterface

class JvmNetworkSys(
    scope: CoroutineScope
) : Network.Sys {

    private val channel = networkCallbacksFlow().onEach { status = it }.broadcastIn(scope)

    override var status: Network.Status = Network.Status.Unavailable

    override fun statusFlow(): Flow<Network.Status> = channel.asFlow()
}

private fun networkCallbacksFlow(wait: Long = 3000) =
    callbackFlow {
        val job = launch {
            while (true) {
                send(currentNetworks())
                delay(wait)
            }
        }
        awaitClose { job.cancel() }
    }.distinctUntilChanged().map { networks ->
        when (networks.isNotEmpty()) {
            true -> Network.Status.Available
            false -> Network.Status.Unavailable
        }
    }

private fun currentNetworks() = NetworkInterface
    .getNetworkInterfaces()
    .toList()
    .filterNot { it.isLoopback }
    .filter { it.isUp }
    .sortedBy { it.index }
    .map { it.inetAddresses.toList().toSet() }



