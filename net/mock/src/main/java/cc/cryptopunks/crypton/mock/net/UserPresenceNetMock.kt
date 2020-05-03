package cc.cryptopunks.crypton.mock.net

import cc.cryptopunks.crypton.context.UserPresence

class UserPresenceNetMock : UserPresence.Net {
    override val getCached = object :
        UserPresence.Net.GetCached {
        override fun invoke(): List<UserPresence> = emptyList()
    }
}
