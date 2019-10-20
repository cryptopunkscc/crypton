package cc.cryptopunks.crypton.api

import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

internal class ApiCache(
    private val map: MutableMap<String, Api> = mutableMapOf()
) :
    Flow<Api> {

    val isEmpty get() = map.isEmpty()
    private val channel = BroadcastChannel<Api?>(Channel.CONFLATED)

    @InternalCoroutinesApi
    override suspend fun collect(collector: FlowCollector<Api>) {
        map.values.asFlow().collect(collector)
        channel.asFlow().filterNotNull().collect(collector)
    }

    suspend fun remove(key: String) = map
        .remove(key)
        ?.apply { send(Api.Empty(address = address)) }

    suspend fun put(key: String, value: Api): Api? = map
        .put(key, value)
        .apply { send(value) }

    fun get(address: String) = map[address]

    fun contains(address: String) = address in map

    private suspend fun send(client: Api) {
        channel.send(client)
        channel.send(null)
    }
}