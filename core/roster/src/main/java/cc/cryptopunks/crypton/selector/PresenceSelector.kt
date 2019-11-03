package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.entity.Address
import cc.cryptopunks.crypton.entity.Presence
import cc.cryptopunks.crypton.manager.PresenceManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PresenceSelector @Inject constructor(
    presenceManager: PresenceManager
) : (Address) -> Flow<Presence.Status> by { address ->
    presenceManager
        .filter { it.address == address }
        .map { it.presence.status }
}