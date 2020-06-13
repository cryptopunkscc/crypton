package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Presence
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull

internal class PresenceFlowSelector(
    userPresenceStore: Presence.Store
) : (Address) -> Flow<Presence.Status> by { address ->
    userPresenceStore.changesFlow().mapNotNull { map: Map<Address, Presence> ->
        map[address]?.status
    }
}
