package cc.cryptopunks.crypton.mock.net

import cc.cryptopunks.crypton.context.Presence
import cc.cryptopunks.crypton.mock.MockState

class PresenceNetMock(
    private val state: MockState
) : Presence.Net {

    override fun sendPresence(presence: Presence) = Unit
    override fun getCachedPresences(): List<Presence> = emptyList()
}
