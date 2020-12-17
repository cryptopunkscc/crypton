package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Presence
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.context.presenceStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull

internal fun SessionScope.presenceFlow(address: Address): Flow<Presence.Status> =
    presenceStore.changesFlow().mapNotNull { map: Map<Address, Presence> ->
        map[address]?.status
    }
