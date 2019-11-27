package cc.cryptopunks.crypton.smack.net.roster

import cc.cryptopunks.crypton.context.RosterEvent
import cc.cryptopunks.crypton.smack.util.SmackJid
import cc.cryptopunks.crypton.smack.util.presence
import cc.cryptopunks.crypton.smack.util.resourceId
import kotlinx.coroutines.channels.SendChannel
import org.jivesoftware.smack.packet.Presence
import org.jivesoftware.smack.roster.PresenceEventListener
import org.jivesoftware.smack.roster.Roster
import org.jivesoftware.smack.roster.RosterLoadedListener
import org.jivesoftware.smack.roster.SubscribeListener
import org.jxmpp.jid.BareJid
import org.jxmpp.jid.FullJid
import org.jxmpp.jid.Jid

class RosterFlowAdapter(
    private val channel: SendChannel<RosterEvent>
) :
    RosterLoadedListener,
    PresenceEventListener,
    SubscribeListener {

    override fun processSubscribe(
        from: SmackJid,
        subscribeRequest: Presence
    ): SubscribeListener.SubscribeAnswer? {
        channel.offer(
            RosterEvent.ProcessSubscribe(
                from.resourceId(),
                subscribeRequest.presence()
            )
        )
        return null
    }

    override fun presenceAvailable(address: FullJid, availablePresence: Presence) {
        channel.offer(
            RosterEvent.PresenceAvailable(
                address.resourceId(),
                availablePresence.presence()
            )
        )
    }

    override fun presenceUnavailable(address: FullJid, presence: Presence) {
        channel.offer(
            RosterEvent.PresenceUnavailable(
                address.resourceId(),
                presence.presence()
            )
        )

    }

    override fun presenceSubscribed(address: BareJid, subscribedPresence: Presence) {
        channel.offer(
            RosterEvent.PresenceSubscribed(
                address.resourceId(),
                subscribedPresence.presence()
            )
        )

    }

    override fun presenceUnsubscribed(address: BareJid, unsubscribedPresence: Presence) {
        channel.offer(
            RosterEvent.PresenceUnsubscribed(
                address.resourceId(),
                unsubscribedPresence.presence()
            )
        )

    }

    override fun presenceError(address: Jid, errorPresence: Presence) {
        channel.offer(
            RosterEvent.PresenceError(
                address.resourceId(),
                errorPresence.presence()
            )
        )

    }

    override fun onRosterLoaded(roster: Roster) {
        channel.offer(
            RosterEvent.RosterLoaded(
                roster
            )
        )

    }

    override fun onRosterLoadingFailed(exception: Exception) {
        channel.offer(
            RosterEvent.RosterLoadingFailed(
                exception
            )
        )
    }
}