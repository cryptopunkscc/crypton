package cc.cryptopunks.crypton.smack.api.user

import cc.cryptopunks.crypton.entity.User
import cc.cryptopunks.crypton.smack.util.remoteId
import org.jivesoftware.smack.roster.Roster

class UserGetContacts(
    roster: Roster
) : User.Api.GetContacts, () -> List<User> by {
    roster.entries.map { entry ->
        User(address = entry.jid.remoteId())
    }
}