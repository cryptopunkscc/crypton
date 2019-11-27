package cc.cryptopunks.crypton.sys

import android.net.ConnectivityManager
import cc.cryptopunks.crypton.context.Network
import cc.cryptopunks.crypton.context.Network.Status.Unavailable
import cc.cryptopunks.crypton.service.Service
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetNetworkStatus @Inject constructor(
    connectivityManager: ConnectivityManager,
    networkCallbacks: NetworkCallbackFlow,
    scope: Service.Scope
) :
    Network.Sys.GetStatus {

    private val channel = ConflatedBroadcastChannel<Network.Status>(Unavailable)

    init {
        scope.launch {
            networkCallbacks
                .map(NetworkStatusProcessor().process)
                .filterNotNull()
                .collect(channel::send)
        }

        connectivityManager.bind(networkCallbacks)
    }

    override fun invoke(): Network.Status =
        channel.value

    @InternalCoroutinesApi
    override suspend fun collect(collector: FlowCollector<Network.Status>) =
        channel.asFlow().collect(collector)

}