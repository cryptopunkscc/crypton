package cc.cryptopunks.crypton.context

data class Presence(
    val status: Status
) {
    interface Net {
        val sendPresence: Send

        interface Send : (Presence) -> Unit
    }

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
            status = Status.Unavailable
        )
    }
}