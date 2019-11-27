package cc.cryptopunks.crypton.manager

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Presence
import cc.cryptopunks.crypton.context.UserPresence
import cc.cryptopunks.crypton.util.Broadcast
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.asFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PresenceManager @Inject constructor() : Flow<UserPresence> {

    private val log = typedLog()
    private val broadcast = Broadcast<UserPresence>()
    private val presenceMap = mutableMapOf<Address, Presence>()

    operator fun set(address: Address, presence: Presence) = synchronized(this) {
        log.d("set: $address $presence")
        presenceMap[address] = presence
        broadcast(UserPresence(address, presence))
    }

    suspend fun send(address: Address, presence: Presence) {
        log.d("send: $address $presence")
        presenceMap[address] = presence
        broadcast.send(
            UserPresence(
                address,
                presence
            )
        )
    }

    operator fun get(address: Address): Presence =
        presenceMap[address] ?: Presence.Empty

    @InternalCoroutinesApi
    override suspend fun collect(collector: FlowCollector<UserPresence>) {
        presenceMap.map { (address, presence) ->
            UserPresence(address, presence)
        }.asFlow().collect(collector)
        broadcast.collect(collector)
    }

    interface Component {
        val presenceManager: PresenceManager
    }
}