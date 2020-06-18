package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Presence
import cc.cryptopunks.crypton.context.SessionScope
import kotlinx.coroutines.flow.mapNotNull

internal fun SessionScope.presenceFlow(address: Address) =
    presenceStore.changesFlow().mapNotNull { map: Map<Address, Presence> ->
        map[address]?.status
    }
