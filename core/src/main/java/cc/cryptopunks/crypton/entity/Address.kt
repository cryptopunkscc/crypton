package cc.cryptopunks.crypton.entity

data class Address(
    val local: String = "",
    val domain: String = ""
) : CharSequence by buildString({
    append(local)
    append("@")
    append(domain)
}) {
    val login get() = local

    val id by lazy { toString() }

    override fun toString(): String = substring(0, length)

    companion object {
        val Empty = Address()

        fun from(string: String) = string.split("@").run {
            Address(
                local = get(0),
                domain = getOrNull(1) ?: ""
            )
        }
    }
}