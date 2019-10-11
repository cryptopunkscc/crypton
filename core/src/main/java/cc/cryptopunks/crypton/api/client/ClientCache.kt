package cc.cryptopunks.crypton.api.client

import cc.cryptopunks.crypton.api.Client
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

internal class ClientCache(
    private val map: MutableMap<String, Client> = mutableMapOf()
) :
    MutableMap<String, Client> by map,
    Flow<Client> {

    private val channel = BroadcastChannel<Client?>(Channel.CONFLATED)

    @InternalCoroutinesApi
    override suspend fun collect(collector: FlowCollector<Client>) {
        map.values.asFlow().collect(collector)
        channel.asFlow().filterNotNull().collect(collector)
    }

    override fun remove(key: String) = map
        .remove(key)
        ?.apply { send(Client.Empty(address = address)) }

    override fun put(key: String, value: Client): Client? = map
        .put(key, value)
        .apply { send(value) }

    private fun send(client: Client) = GlobalScope.launch {
        channel.send(client)
        channel.send(null)
    }
}