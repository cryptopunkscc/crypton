package cc.cryptopunks.crypton.mock

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Presence
import cc.cryptopunks.crypton.mock.MockState

class MockPresenceNet(
    private val state: MockState
) : Presence.Net {

    override fun sendPresence(presence: Presence) = Unit
    override fun getCachedPresences(): List<Presence> = emptyList()
    override fun iAmSubscribed(address: Address): Boolean = false
}
