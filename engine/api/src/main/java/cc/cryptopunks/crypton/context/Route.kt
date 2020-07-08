package cc.cryptopunks.crypton.context

sealed class Route {
    object Main : Route()
    object Back : Route()
    data class Chat(
        val account: Address,
        val address: Address
    ) : Route() {
        constructor(account: String = "", address: String = "") : this(
            account = address(account),
            address = address(address)
        )
    }

    override fun equals(other: Any?): Boolean = this::class == other?.let { it::class }
    override fun hashCode(): Int = this::class.hashCode()
    override fun toString(): String = this::class.qualifiedName!!
}
