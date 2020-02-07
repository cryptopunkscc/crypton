package cc.cryptopunks.crypton.smack.net.roster

import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.smack.util.SmackJid
import cc.cryptopunks.crypton.smack.util.presence
import cc.cryptopunks.crypton.smack.util.resourceId
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
                from.resourceId(),
                subscribeRequest.presence()
            )
        )
        return null
    }

    override fun presenceAvailable(address: FullJid, availablePresence: Presence) {
        channel.offer(
            Roster.Net.PresenceChanged(
                address.resourceId(),
                availablePresence.presence()
            )
        )
    }

    override fun presenceUnavailable(address: FullJid, presence: Presence) {
        channel.offer(
            Roster.Net.PresenceChanged(
                address.resourceId(),
                presence.presence()
            )
        )
    }

    override fun presenceSubscribed(address: BareJid, subscribedPresence: Presence) {
        channel.offer(
            Roster.Net.PresenceChanged(
                address.resourceId(),
                subscribedPresence.presence()
            )
        )
    }

    override fun presenceUnsubscribed(address: BareJid, unsubscribedPresence: Presence) {
        channel.offer(
            Roster.Net.PresenceChanged(
                address.resourceId(),
                unsubscribedPresence.presence()
            )
        )
    }

    override fun presenceError(address: Jid, errorPresence: Presence) {
        channel.offer(
            Roster.Net.PresenceChanged(
                resource = address.resourceId(),
                presence = errorPresence.presence()
            )
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