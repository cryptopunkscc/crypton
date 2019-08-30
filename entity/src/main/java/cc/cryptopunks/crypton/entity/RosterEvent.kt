package cc.cryptopunks.crypton.entity

import cc.cryptopunks.crypton.util.RxPublisher


sealed class RosterEvent {

    interface Api {
        val rosterEventPublisher: Publisher

        interface Publisher : RxPublisher<RosterEvent>
    }

    data class ProcessSubscribe(
        val from: RemoteId,
        val subscribeRequest: Presence
    ) : RosterEvent()

    data class PresenceAvailable(
        val address: RemoteId,
        val availablePresence: Presence
    ) : RosterEvent()

    data class PresenceUnavailable(
        val address: RemoteId,
        val presence: Presence
    ) : RosterEvent()

    data class PresenceSubscribed(
        val address: RemoteId,
        val subscribedPresence: Presence
    ) : RosterEvent()

    data class PresenceUnsubscribed(
        val address: RemoteId,
        val unsubscribedPresence: Presence
    ) : RosterEvent()

    data class PresenceError(
        val address: RemoteId,
        val errorPresence: Presence
    ) : RosterEvent()

    data class RosterLoaded(
        val roster: Any
    ) : RosterEvent()

    data class RosterLoadingFailed(
        val exception: Exception
    ) : RosterEvent()

    data class EntriesDeleted(
        val addresses: Collection<RemoteId>
    ) : RosterEvent()

    data class PresenceChanged(
        val presence: Presence
    ) : RosterEvent()

    data class EntriesUpdated(
        val addresses: Collection<RemoteId>
    ) : RosterEvent()

    data class EntriesAdded(
        val addresses: Collection<RemoteId>
    ) : RosterEvent()

}