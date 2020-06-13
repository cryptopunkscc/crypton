package cc.cryptopunks.crypton.smack.net.presence

import cc.cryptopunks.crypton.context.Presence
import cc.cryptopunks.crypton.smack.util.presence
import org.jivesoftware.smack.roster.Roster

class GetCachedPresences(
    roster: Roster
) : () -> List<Presence> by {
    roster.run {
        entries.map { entry ->
            getPresence(entry.jid).presence(entry.jid)
        }
    }
}
