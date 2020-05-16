package cc.cryptopunks.crypton.mock.net

import cc.cryptopunks.crypton.context.UserPresence

class UserPresenceNetMock : UserPresence.Net {
    override fun getCached(): List<UserPresence> = emptyList()
}
