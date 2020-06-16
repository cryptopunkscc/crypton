package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.util.OpenStore

data class Presence(
    val resource: Resource,
    val status: Status
) {
    interface Net {
        fun sendPresence(presence: Presence)
        fun getCachedPresences(): List<Presence>
        fun iAmSubscribed(address: Address): Boolean
    }

    class Store : OpenStore<Map<Address, Presence>>(emptyMap())

    enum class Status {
        /** The user is available to receive messages (default). */
        Available,

        /** The user is unavailable to receive messages. */
        Unavailable,

        /** Request subscription to recipient's presence. */
        Subscribe,

        /** Grant subscription to sender's presence. */
        Subscribed,

        /** Request removal of subscription to sender's presence. */
        Unsubscribe,

        /** Grant removal of subscription to sender's presence. */
        Unsubscribed,

        /** The presence stanza contains an error message. */
        Error,

        /** A presence probe as defined in section 4.3 of RFC 6121. */
        Probe
    }

    companion object {
        val Empty = Presence(
            resource = Resource.Empty,
            status = Status.Unavailable
        )
    }
}
