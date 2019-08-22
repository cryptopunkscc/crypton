package cc.cryptopunks.crypton.smack.roster

import cc.cryptopunks.crypton.api.ApiQualifier
import cc.cryptopunks.crypton.api.ApiScope
import cc.cryptopunks.crypton.api.entities.RosterEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.internal.disposables.CancellableDisposable
import org.jivesoftware.smack.roster.Roster
import org.reactivestreams.Publisher
import javax.inject.Inject

@ApiScope
internal class RosterEventPublisher @Inject constructor(
    @ApiQualifier disposable: CompositeDisposable,
    roster: Roster,
    adapter: RosterRxAdapter
) :
    RosterEvent.Publisher,
    Publisher<RosterEvent> by adapter {

    init {
        roster.apply {
            subscriptionMode = Roster.SubscriptionMode.reject_all
            disposable.addAll(
                CancellableDisposable {
//                    removeRosterListener(adapter)
                    removeRosterLoadedListener(adapter)
                    removePresenceEventListener(adapter)
                    removeSubscribeListener(adapter)
                }
            )

//            addRosterListener(adapter)
            addRosterLoadedListener(adapter)
            addPresenceEventListener(adapter)
            addSubscribeListener(adapter)
        }
    }
}