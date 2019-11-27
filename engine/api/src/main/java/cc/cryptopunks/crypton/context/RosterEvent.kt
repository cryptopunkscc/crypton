package cc.cryptopunks.crypton.context

import kotlinx.coroutines.flow.Flow

sealed class RosterEvent(
    open val resource: Resource? = null,
    open val presence: Presence? = null
) {

    interface Net {
        val rosterEventPublisher: Broadcast

        interface Broadcast : Flow<RosterEvent>
    }

    data class ProcessSubscribe(
        val from: Resource,
        val subscribeRequest: Presence
    ) : RosterEvent(
        resource = from,
        presence = subscribeRequest
    )

    data class PresenceAvailable(
        override val resource: Resource,
        val availablePresence: Presence
    ) : RosterEvent(
        presence = availablePresence
    )

    data class PresenceUnavailable(
        override val resource: Resource,
        override val presence: Presence
    ) : RosterEvent()

    data class PresenceSubscribed(
        override val resource: Resource,
        val subscribedPresence: Presence
    ) : RosterEvent(
        presence = subscribedPresence
    )

    data class PresenceUnsubscribed(
        override val resource: Resource,
        val unsubscribedPresence: Presence
    ) : RosterEvent(
        presence = unsubscribedPresence
    )

    data class PresenceError(
        override val resource: Resource,
        val errorPresence: Presence
    ) : RosterEvent(
        presence = errorPresence
    )

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
        override val presence: Presence
    ) : RosterEvent()

    data class EntriesUpdated(
        val addresses: Collection<Resource>
    ) : RosterEvent()

    data class EntriesAdded(
        val addresses: Collection<Resource>
    ) : RosterEvent()

}