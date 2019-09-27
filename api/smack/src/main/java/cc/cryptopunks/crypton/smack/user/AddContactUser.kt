package cc.cryptopunks.crypton.smack.user

import cc.cryptopunks.crypton.api.ApiScope
import cc.cryptopunks.crypton.entity.User
import cc.cryptopunks.crypton.smack.bareJid
import org.jivesoftware.smack.roster.Roster
import javax.inject.Inject

@ApiScope
class AddContactUser @Inject constructor(
    roster: Roster
) : User.Api.AddContact, (User) -> Unit by { user ->
    roster.createEntry(
        user.remoteId.bareJid(),
        user.remoteId.local,
        null
    )
}