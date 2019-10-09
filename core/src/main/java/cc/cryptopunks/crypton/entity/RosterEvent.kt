package cc.cryptopunks.crypton.entity

import kotlinx.coroutines.flow.Flow

sealed class RosterEvent {

    interface Api {
        val rosterEventPublisher: Broadcast

        interface Broadcast : Flow<RosterEvent>
    }

    data class ProcessSubscribe(
        val from: Resource,
        val subscribeRequest: Presence
    ) : RosterEvent()

    data class PresenceAvailable(
        val address: Resource,
        val availablePresence: Presence
    ) : RosterEvent()

    data class PresenceUnavailable(
        val address: Resource,
        val presence: Presence
    ) : RosterEvent()

    data class PresenceSubscribed(
        val address: Resource,
        val subscribedPresence: Presence
    ) : RosterEvent()

    data class PresenceUnsubscribed(
        val address: Resource,
        val unsubscribedPresence: Presence
    ) : RosterEvent()

    data class PresenceError(
        val address: Resource,
        val errorPresence: Presence
    ) : RosterEvent()

    data class RosterLoaded(
        val roster: Any
    ) : RosterEvent()

    data class RosterLoadingFailed(
        val exception: Exception
    ) : RosterEvent()

    data class EntriesDeleted(
        val addresses: Collection<Resource>
    ) : RosterEvent()

    data class PresenceChanged(
        val presence: Presence
    ) : RosterEvent()

    data class EntriesUpdated(
        val addresses: Collection<Resource>
    ) : RosterEvent()

    data class EntriesAdded(
        val addresses: Collection<Resource>
    ) : RosterEvent()

}