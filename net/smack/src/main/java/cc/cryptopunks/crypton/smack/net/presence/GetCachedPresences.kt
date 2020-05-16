package cc.cryptopunks.crypton.smack.net.presence

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.UserPresence
import cc.cryptopunks.crypton.smack.util.presence
import org.jivesoftware.smack.roster.Roster

class GetCachedPresences(
    roster: Roster
) : () -> List<UserPresence> by {
    roster.run {
        entries.map { entry ->
            UserPresence(
                address = Address.from(entry.jid.toString()),
                presence = getPresence(entry.jid).presence()
            )
        }
    }
}
