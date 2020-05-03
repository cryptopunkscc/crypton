package cc.cryptopunks.crypton.context

data class Address(
    val local: String = "",
    val domain: String = ""
) : CharSequence by buildString({
    append(local)
    append("@")
    append(domain)
}) {
    val id by lazy { toString() }

    fun validate() {
        local.isNotBlank() || throw Exception.InvalidAddress
        domain.split('.').size == 2 || throw Exception.InvalidAddress
    }

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

    class Exception(message: String?) : kotlin.Exception(message) {
        companion object {
            val InvalidAddress = Exception("Invalid address")
        }
    }
}
