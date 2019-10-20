package cc.cryptopunks.crypton.net

import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

internal class NetCache(
    private val map: MutableMap<String, Net> = mutableMapOf()
) :
    Flow<Net> {

    val isEmpty get() = map.isEmpty()
    private val channel = BroadcastChannel<Net?>(Channel.CONFLATED)

    @InternalCoroutinesApi
    override suspend fun collect(collector: FlowCollector<Net>) {
        map.values.asFlow().collect(collector)
        channel.asFlow().filterNotNull().collect(collector)
    }

    suspend fun remove(key: String) = map
        .remove(key)
        ?.apply { send(Net.Empty(address = address)) }

    suspend fun put(key: String, value: Net): Net? = map
        .put(key, value)
        .apply { send(value) }

    fun get(address: String) = map[address]

    fun contains(address: String) = address in map

    private suspend fun send(client: Net) {
        channel.send(client)
        channel.send(null)
    }
}