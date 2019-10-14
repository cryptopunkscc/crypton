package cc.cryptopunks.crypton.api.client

import cc.cryptopunks.crypton.api.Client
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

internal class ClientCache(
    private val map: MutableMap<String, Client> = mutableMapOf()
) :
    Flow<Client> {

    private val channel = BroadcastChannel<Client?>(Channel.CONFLATED)

    @InternalCoroutinesApi
    override suspend fun collect(collector: FlowCollector<Client>) {
        map.values.asFlow().collect(collector)
        channel.asFlow().filterNotNull().collect(collector)
    }

    suspend fun remove(key: String) = map
        .remove(key)
        ?.apply { send(Client.Empty(address = address)) }

    suspend fun put(key: String, value: Client): Client? = map
        .put(key, value)
        .apply { send(value) }

    fun get(address: String) = map[address]

    fun contains(address: String) = address in map

    private suspend fun send(client: Client) {
        channel.send(client)
        channel.send(null)
    }
}