package cc.cryptopunks.crypton.smack.roster

import cc.cryptopunks.crypton.entity.RosterEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.internal.disposables.CancellableDisposable
import org.jivesoftware.smack.roster.Roster
import org.reactivestreams.Publisher

internal class RosterEventPublisher(
    disposable: CompositeDisposable,
    roster: Roster,
    adapter: RosterRxAdapter
) :
    RosterEvent.Api.Publisher,
    Publisher<RosterEvent> by adapter {

    init {
        roster.apply {
            subscriptionMode = Roster.SubscriptionMode.reject_all
            disposable.addAll(
                CancellableDisposable {
                    removeRosterLoadedListener(adapter)
                    removePresenceEventListener(adapter)
                    removeSubscribeListener(adapter)
                }
            )

            addRosterLoadedListener(adapter)
            addPresenceEventListener(adapter)
            addSubscribeListener(adapter)
        }
    }
}