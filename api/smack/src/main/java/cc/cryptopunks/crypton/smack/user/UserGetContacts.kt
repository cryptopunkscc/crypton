package cc.cryptopunks.crypton.smack.user

import cc.cryptopunks.crypton.entity.User
import cc.cryptopunks.crypton.smack.remoteId
import org.jivesoftware.smack.roster.Roster

class UserGetContacts(
    roster: Roster
) : User.Api.GetContacts, () -> List<User> by {
    roster.entries.map { entry ->
        User(address = entry.jid.remoteId())
    }
}