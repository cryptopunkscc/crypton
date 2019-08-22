package cc.cryptopunks.crypton.smack.user

import cc.cryptopunks.crypton.api.ApiScope
import cc.cryptopunks.crypton.api.entities.User
import cc.cryptopunks.crypton.smack.jid
import org.jivesoftware.smack.roster.Roster
import javax.inject.Inject

@ApiScope
class UserGetContacts @Inject constructor(
    roster: Roster
) : User.GetContacts, () -> List<User> by {
    roster.entries.map { entry ->
        User(
            jid = entry.jid.jid()
        )
    }
}