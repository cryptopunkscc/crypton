package cc.cryptopunks.crypton.entity

data class RemoteId(
    val local: String = "",
    val domain: String = ""
) : CharSequence by buildString({
    if (local.isNotEmpty()) {
        append(local)
        append("@")
    }
    append(domain)
}) {
    val login get() = local

    override fun toString(): String = substring(0, length)

    companion object {
        val Empty = RemoteId()

        fun from(string: String) = string.split("@").run {
            RemoteId(
                local = get(0),
                domain = get(1)
            )
        }
    }
}