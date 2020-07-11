package cc.cryptopunks.crypton.context

sealed class Route<R : Route<R>> {
    open fun empty(): R = this as R

    object Main : Route<Main>()

    object Back : Route<Back>()

    data class Chat(
        val account: Address = Address.Empty,
        val address: Address = Address.Empty,
        val isConference: Boolean = address.isConference
    ) : Route<Chat>() {
        override fun empty() = Empty.copy(isConference = isConference)
        constructor(account: String = "", address: String = "") : this(
            account = address(account),
            address = address(address)
        )
        companion object {
            val Empty = Chat()
        }
    }

    override fun equals(other: Any?): Boolean = this::class == other?.let { it::class }
    override fun hashCode(): Int = this::class.hashCode()
    override fun toString(): String = this::class.qualifiedName!!
}
