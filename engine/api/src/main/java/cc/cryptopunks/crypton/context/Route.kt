package cc.cryptopunks.crypton.context

import kotlinx.coroutines.runBlocking

sealed class Route {
    override fun equals(other: Any?): Boolean = this::class == other?.let { it::class }
    override fun hashCode(): Int = this::class.hashCode()
    override fun toString(): String = this::class.qualifiedName!!

    object Main : Route()
    object Back : Route()
    class Chat(
        val account: Address,
        val address: Address
    ) : Route() {
        constructor(account: String = "", address: String = "") : this(
            account = address(account),
            address = address(address)
        )
    }

    interface Sys {
        fun navigate(route: Route)
        suspend fun bind(navigator: Any)
    }

    class Navigate(private val sys: Sys) : (Route) -> Unit {
        override fun invoke(route: Route) = runBlocking { sys.navigate(route) }
    }
}
