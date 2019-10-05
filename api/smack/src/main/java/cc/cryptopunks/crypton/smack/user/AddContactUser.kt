package cc.cryptopunks.crypton.smack.user

import cc.cryptopunks.crypton.entity.User
import cc.cryptopunks.crypton.smack.bareJid
import org.jivesoftware.smack.roster.Roster

class AddContactUser(
    roster: Roster
) : User.Api.AddContact, (User) -> Unit by { user ->
    roster.createEntry(
        user.address.bareJid(),
        user.address.local,
        null
    )
}