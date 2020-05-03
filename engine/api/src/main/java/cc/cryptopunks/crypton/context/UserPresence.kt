package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.util.OpenStore

data class UserPresence(
    val address: Address,
    val presence: Presence
) {
    interface Net {
        val getCached: GetCached
        interface GetCached : () -> List<UserPresence>
    }

    class Store : OpenStore<Map<Address, Presence>>(emptyMap())
}
