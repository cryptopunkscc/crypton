package cc.cryptopunks.crypton.manager

import cc.cryptopunks.crypton.entity.Address
import cc.cryptopunks.crypton.entity.Presence
import cc.cryptopunks.crypton.entity.UserPresence
import cc.cryptopunks.crypton.util.Broadcast
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector

class PresenceManager : Flow<UserPresence> {

    private val broadcast = Broadcast<UserPresence>()
    private val presenceMap = mutableMapOf<Address, Presence>()

    operator fun set(address: Address, presence: Presence) {
        presenceMap[address] = presence
        broadcast(UserPresence(address, presence))
    }

    operator fun get(address: Address): Presence =
        presenceMap[address] ?: Presence.Empty

    @InternalCoroutinesApi
    override suspend fun collect(collector: FlowCollector<UserPresence>) =
        broadcast.collect(collector)
}