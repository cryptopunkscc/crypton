package cc.cryptopunks.crypton.smack.net.roster

import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.smack.util.SmackJid
import cc.cryptopunks.crypton.smack.util.presence
import kotlinx.coroutines.channels.SendChannel
import org.jivesoftware.smack.packet.Presence
import org.jivesoftware.smack.roster.PresenceEventListener
import org.jivesoftware.smack.roster.RosterLoadedListener
import org.jivesoftware.smack.roster.SubscribeListener
import org.jxmpp.jid.BareJid
import org.jxmpp.jid.FullJid
import org.jxmpp.jid.Jid

class RosterFlowAdapter(
    private val channel: SendChannel<Roster.Net.Event>
) :
    RosterLoadedListener,
    PresenceEventListener,
    SubscribeListener {

    override fun processSubscribe(
        from: SmackJid,
        subscribeRequest: Presence
    ): SubscribeListener.SubscribeAnswer? {
        channel.offer(
            Roster.Net.PresenceChanged(
                subscribeRequest.presence(from)
            )
        )
        return null
    }

    override fun presenceAvailable(address: FullJid?, availablePresence: Presence) {
        channel.offer(
            Roster.Net.PresenceChanged(
                availablePresence.presence(address)
            )
        )
    }

    override fun presenceUnavailable(address: FullJid, presence: Presence) {
        channel.offer(
            Roster.Net.PresenceChanged(
                presence.presence(address)
            )
        )
    }

    override fun presenceSubscribed(address: BareJid, subscribedPresence: Presence) {
        channel.offer(
            Roster.Net.PresenceChanged(
                subscribedPresence.presence(address)
            )
        )
    }

    override fun presenceUnsubscribed(address: BareJid, unsubscribedPresence: Presence) {
        channel.offer(
            Roster.Net.PresenceChanged(
                unsubscribedPresence.presence(address)
            )
        )
    }

    override fun presenceError(address: Jid, errorPresence: Presence) {
        channel.offer(
            Roster.Net.PresenceChanged(errorPresence.presence(address))
        )
    }

    override fun onRosterLoaded(roster: org.jivesoftware.smack.roster.Roster) {
        channel.offer(
            Roster.Net.Loading.Success(roster)
        )

    }

    override fun onRosterLoadingFailed(exception: Exception) {
        channel.offer(
            Roster.Net.Loading.Failed(exception)
        )
    }
}
