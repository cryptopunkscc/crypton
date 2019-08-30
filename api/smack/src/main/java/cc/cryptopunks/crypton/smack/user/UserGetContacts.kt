package cc.cryptopunks.crypton.smack.user

import cc.cryptopunks.crypton.api.ApiScope
import cc.cryptopunks.crypton.entity.User
import cc.cryptopunks.crypton.smack.remoteId
import org.jivesoftware.smack.roster.Roster
import javax.inject.Inject

@ApiScope
class UserGetContacts @Inject constructor(
    roster: Roster
) : User.Api.GetContacts, () -> List<User> by {
    roster.entries.map { entry ->
        User(
            remoteId = entry.jid.remoteId()
        )
    }
}