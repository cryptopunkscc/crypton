package cc.cryptopunks.crypton.smack.net.user

import cc.cryptopunks.crypton.context.User
import cc.cryptopunks.crypton.smack.util.bareJid
import org.jivesoftware.smack.roster.Roster

class AddContactUser(
    roster: Roster
) : User.Net.AddContact, (User) -> Unit by { user ->
    roster.createEntry(
        user.address.bareJid(),
        user.address.local,
        null
    )
}