package cc.cryptopunks.crypton.context

data class Resource(
    val address: Address = Address.Empty,
    val resource: String = ""
) : CharSequence by buildString({
    append(address)
    if (resource.isNotEmpty()) {
        append("/")
        append(resource)
    }
}) {

    val id by lazy { toString() }

    override fun toString(): String = substring(0, length)

    companion object {
        val Empty = Resource()

        fun from(string: String) = string.split("/").run {
            Resource(
                address = Address.from(
                    get(0)
                ),
                resource = getOrNull(1) ?: ""
            )
        }
    }
}