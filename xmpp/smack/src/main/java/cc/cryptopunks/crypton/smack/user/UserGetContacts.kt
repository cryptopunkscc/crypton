package cc.cryptopunks.crypton.smack.user

import cc.cryptopunks.crypton.xmpp.XmppScope
import cc.cryptopunks.crypton.xmpp.entities.User
import cc.cryptopunks.crypton.smack.jid
import org.jivesoftware.smack.roster.Roster
import javax.inject.Inject

@XmppScope
class UserGetContacts @Inject constructor(
    roster: Roster
) : User.GetContacts, () -> List<User> by {
    roster.entries.map { entry ->
        User(
            jid = entry.jid.jid()
        )
    }
}