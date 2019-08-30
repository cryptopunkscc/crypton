package cc.cryptopunks.crypton.smack.roster

import cc.cryptopunks.crypton.entity.RosterEvent
import cc.cryptopunks.crypton.smack.SmackJid
import cc.cryptopunks.crypton.smack.remoteId
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
import javax.inject.Inject

class RosterRxAdapter private constructor(
    private val processor: Processor<RosterEvent, RosterEvent>
) :
    Publisher<RosterEvent> by processor,
//    RosterListener,
    RosterLoadedListener,
    PresenceEventListener,
    SubscribeListener {

    @Inject
    constructor() : this(PublishProcessor.create())


    override fun processSubscribe(
        from: SmackJid,
        subscribeRequest: Presence
    ): SubscribeListener.SubscribeAnswer? {
        processor.onNext(
            RosterEvent.ProcessSubscribe(
                from.remoteId(),
                subscribeRequest.presence()
            )
        )
        return null
    }

    override fun presenceAvailable(address: FullJid, availablePresence: Presence) =
        processor.onNext(
            RosterEvent.PresenceAvailable(
                address.remoteId(),
                availablePresence.presence()
            )
        )


    override fun presenceUnavailable(address: FullJid, presence: Presence) =
        processor.onNext(
            RosterEvent.PresenceUnavailable(
                address.remoteId(),
                presence.presence()
            )
        )


    override fun presenceSubscribed(address: BareJid, subscribedPresence: Presence) =
        processor.onNext(
            RosterEvent.PresenceSubscribed(
                address.remoteId(),
                subscribedPresence.presence()
            )
        )


    override fun presenceUnsubscribed(address: BareJid, unsubscribedPresence: Presence) =
        processor.onNext(
            RosterEvent.PresenceUnsubscribed(
                address.remoteId(),
                unsubscribedPresence.presence()
            )
        )


    override fun presenceError(address: Jid, errorPresence: Presence) =
        processor.onNext(
            RosterEvent.PresenceError(
                address.remoteId(),
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


//    override fun entriesDeleted(addresses: Collection<Jid>) =
//        processor.onNext(
//            RosterEvent.EntriesDeleted(
//                addresses.map(Jid::remoteId)
//            )
//        )
//
//
//    override fun presenceChanged(presence: Presence) =
//        processor.onNext(
//            RosterEvent.PresenceChanged(
//                presence.presence()
//            )
//        )
//
//
//    override fun entriesUpdated(addresses: Collection<Jid>) =
//        processor.onNext(
//            RosterEvent.EntriesUpdated(
//                addresses.map(Jid::remoteId)
//            )
//        )
//
//
//    override fun entriesAdded(addresses: Collection<Jid>) =
//        processor.onNext(
//            RosterEvent.EntriesAdded(
//                addresses.map(Jid::remoteId)
//            )
//        )
}