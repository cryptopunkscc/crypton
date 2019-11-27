package cc.cryptopunks.crypton.entity

data class Address(
    val local: String = "",
    val domain: String = ""
) : CharSequence by buildString({
    append(local)
    append("@")
    append(domain)
}) {

    fun validate() {
        local.isNotBlank() || throw Exception.InvalidAddress
        domain.split('.').size == 2 || throw Exception.InvalidAddress
    }

    val id by lazy { toString() }

    override fun toString(): String = substring(0, length)


    @Suppress("NOTHING_TO_INLINE")
    inline fun accountException(throwable: Throwable): Account.Exception =
        if (throwable is Account.Exception) throwable
        else Account.Exception(this, throwable)

    class Exception(message: String?) : kotlin.Exception(message) {
        companion object {
            val InvalidAddress = Exception("Invalid address")
        }
    }

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