package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.util.OpenStore

fun address(string: String) = Address.from(string)

data class Address(
    val local: String,
    val domain: String
) {
    private val string = buildString {
        append(local)
        append("@")
        append(domain)
    }

    val id get() = string

    fun validate() {
        require(local.isNotBlank())
        require(domain.isNotBlank())
    }

    override fun toString(): String = string

    companion object {
        val Empty = Address("", "")

        fun from(string: String) = string.split("@").run {
            Address(
                local = get(0),
                domain = getOrNull(1) ?: ""
            )
        }
    }

    object Subscriptions {
        class Store: OpenStore<Set<Address>>(emptySet())
    }

    class Exception(message: String?) : kotlin.Exception(message) {
        companion object {
            val InvalidAddress = Exception("Invalid address")
        }
    }
}

fun Address.conference() = copy(
    domain = if (domain.startsWith(CONFERENCE_PREFIX))
        domain else CONFERENCE_PREFIX + domain
)

val Address.isConference get() = domain.startsWith(CONFERENCE_PREFIX)

private const val CONFERENCE_PREFIX = "conference."
