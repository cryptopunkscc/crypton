package cc.cryptopunks.crypton.api.entities

import cc.cryptopunks.crypton.common.RxPublisher


sealed class RosterEvent {

    interface Publisher : RxPublisher<RosterEvent>

    data class ProcessSubscribe(
        val from: Jid,
        val subscribeRequest: Presence
    ) : RosterEvent()

    data class PresenceAvailable(
        val address: Jid,
        val availablePresence: Presence
    ) : RosterEvent()

    data class PresenceUnavailable(
        val address: Jid,
        val presence: Presence
    ) : RosterEvent()

    data class PresenceSubscribed(
        val address: Jid,
        val subscribedPresence: Presence
    ) : RosterEvent()

    data class PresenceUnsubscribed(
        val address: Jid,
        val unsubscribedPresence: Presence
    ) : RosterEvent()

    data class PresenceError(
        val address: Jid,
        val errorPresence: Presence
    ) : RosterEvent()

    data class RosterLoaded(
        val roster: Any
    ) : RosterEvent()

    data class RosterLoadingFailed(
        val exception: Exception
    ) : RosterEvent()

    data class EntriesDeleted(
        val addresses: Collection<Jid>
    ) : RosterEvent()

    data class PresenceChanged(
        val presence: Presence
    ) : RosterEvent()

    data class EntriesUpdated(
        val addresses: Collection<Jid>
    ) : RosterEvent()

    data class EntriesAdded(
        val addresses: Collection<Jid>
    ) : RosterEvent()

}