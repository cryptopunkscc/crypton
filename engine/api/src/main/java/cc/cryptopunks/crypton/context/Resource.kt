package cc.cryptopunks.crypton.context

data class Resource(
    val address: Address = Address.Empty,
    val resource: String = ""
) {
    private val string = buildString {
        append(address)
        if (resource.isNotEmpty()) {
            append("/")
            append(resource)
        }
    }

    val id get() = toString()

    override fun toString(): String = string

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
