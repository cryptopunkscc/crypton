package cc.cryptopunks.crypton.api.entities

import java.io.Serializable

data class Jid(
    val local: String = "",
    val domain: String = "",
    val resource: String = ""
) : Serializable,
    CharSequence by buildString({

    if (local.isNotEmpty()) {
        append(local)
        append("@")
    }
    append(domain)

    if (resource.isNotEmpty()) {
        append("/")
        append(resource)
    }
}) {
    val login get() = local

    val withoutResource get() = copy(resource = "")

    override fun toString(): String = substring(0, length)

    companion object {
        val Empty = Jid()
    }
}