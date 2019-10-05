package cc.cryptopunks.crypton.smack.roster

import cc.cryptopunks.crypton.entity.RosterEvent
import cc.cryptopunks.crypton.smack.SmackJid
import cc.cryptopunks.crypton.smack.resourceId
import cc.cryptopunks.crypton.smack.presence
import io.reactivex.processors.PublishProcessor
import org.jivesoftware.smack.packet.Presence
import org.jivesoftware.smack.roster.PresenceEventListener
import org.jivesoftware.smack.roster.Roster
import org.jivesoftware.smack.roster.RosterLoadedListener
import org.jivesoftware.smack.roster.SubscribeListener
import org.jxmpp.jid.BareJid
import org.jxmpp.jid.FullJid
import org.jxmpp.jid.Jid
import org.reactivestreams.Processor
import org.reactivestreams.Publisher

class RosterRxAdapter private constructor(
    private val processor: Processor<RosterEvent, RosterEvent>
) :
    Publisher<RosterEvent> by processor,
    RosterLoadedListener,
    PresenceEventListener,
    SubscribeListener {

    constructor() : this(PublishProcessor.create())


    override fun processSubscribe(
        from: SmackJid,
        subscribeRequest: Presence
    ): SubscribeListener.SubscribeAnswer? {
        processor.onNext(
            RosterEvent.ProcessSubscribe(
                from.resourceId(),
                subscribeRequest.presence()
            )
        )
        return null
    }

    override fun presenceAvailable(address: FullJid, availablePresence: Presence) =
        processor.onNext(
            RosterEvent.PresenceAvailable(
                address.resourceId(),
                availablePresence.presence()
            )
        )


    override fun presenceUnavailable(address: FullJid, presence: Presence) =
        processor.onNext(
            RosterEvent.PresenceUnavailable(
                address.resourceId(),
                presence.presence()
            )
        )


    override fun presenceSubscribed(address: BareJid, subscribedPresence: Presence) =
        processor.onNext(
            RosterEvent.PresenceSubscribed(
                address.resourceId(),
                subscribedPresence.presence()
            )
        )


    override fun presenceUnsubscribed(address: BareJid, unsubscribedPresence: Presence) =
        processor.onNext(
            RosterEvent.PresenceUnsubscribed(
                address.resourceId(),
                unsubscribedPresence.presence()
            )
        )


    override fun presenceError(address: Jid, errorPresence: Presence) =
        processor.onNext(
            RosterEvent.PresenceError(
                address.resourceId(),
                errorPresence.presence()
            )
        )


    override fun onRosterLoaded(roster: Roster) =
        processor.onNext(
            RosterEvent.RosterLoaded(
                roster
            )
        )


    override fun onRosterLoadingFailed(exception: Exception) =
        processor.onNext(
            RosterEvent.RosterLoadingFailed(
                exception
            )
        )
}