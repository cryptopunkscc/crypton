@file:Suppress("EnumEntryName")

package cc.cryptopunks.crypton.context

data class Jingle(
    val sessionId: String = "",
    val action: Action = Action.none,
    val reason: Reason = Reason.none,
    val initiator: Resource = Resource.Empty,
    val responder: Resource = Resource.Empty,
    val from: Address = Address.Empty,
    val to: Resource = Resource.Empty,
    val contents: List<Content> = emptyList()
) : Stanza {

    enum class Action {
        content_accept,
        content_add,
        content_modify,
        content_reject,
        content_remove,
        description_info,
        security_info,
        session_accept,
        session_info,
        session_initiate,
        session_terminate,
        transport_accept,
        transport_info,
        transport_reject,
        transport_replace,
        none
    }

    enum class Reason {
        alternative_session,
        busy,
        cancel,
        connectivity_error,
        decline,
        expired,
        failed_application,
        failed_transport,
        general_error,
        gone,
        incompatible_parameters,
        media_error,
        security_error,
        success,
        timeout,
        unsupported_applications,
        unsupported_transports,
        none
    }

    data class Content(
        val creator: Creator,
        val disposition: String = "",
        val name: String,
        val senders: Senders = Senders.none,
        val description: Description = Description.Empty,
        val transport: Transport = Transport.Empty
    ) {
        enum class Creator {
            initiator,
            responder
        }

        enum class Senders {
            both,
            initiator,
            none,
            responder
        }

        data class Description(
            val payloads: List<String> = emptyList()
        ) {
            companion object {
                val Empty = Description()
            }
        }

        data class Transport(
            val candidates: List<Candidate> = emptyList(),
            val info: List<Info> = emptyList()
        ) {
            interface Candidate :Element
            interface Info : Element
            companion object {
                val Empty = Transport()
            }
        }
    }
}

interface Element {
}

interface Stanza : Element

interface IQ : Stanza {
    val type: Type

    enum class Type {

        /**
         * The IQ stanza requests information, inquires about what data is needed in order to complete further operations, etc.
         */
        get,

        /**
         * The IQ stanza provides data that is needed for an operation to be completed, sets new values, replaces existing values, etc.
         */
        set,

        /**
         * The IQ stanza is a response to a successful get or set request.
         */
        result,

        /**
         * The IQ stanza reports an error that has occurred regarding processing or delivery of a get or set request.
         */
        error,
    }
}
