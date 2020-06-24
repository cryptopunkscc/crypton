package cc.cryptopunks.crypton.context

import kotlinx.coroutines.runBlocking

sealed class Route(
    open val data: MutableMap<String, Any?> = mutableMapOf()
) {
    override fun equals(other: Any?): Boolean = this::class == other?.let { it::class }
    override fun hashCode(): Int = this::class.hashCode()
    override fun toString(): String = this::class.qualifiedName!!

    class Raw(val id: Int) : Route()
    object Main : Route()
    object Back : Route()
    object SetAccount : Route()
    object Login : Route()
    object Register : Route()
    object Dashboard : Route()
    object Roster : Route()
    object AccountList : Route()
    object AccountManagement : Route()
    class CreateChat(data: MutableMap<String, Any?> = mutableMapOf()) : Route(data) {
        var accountId: String by data
        val accountAddress get() = Address.from(accountId)
        override fun toString() = "Route.CreateChat(data=${data})"
    }
    class Chat(data: MutableMap<String, Any?> = mutableMapOf()) : Route(data) {
        var accountId: String by data
        var chatAddress: String by data
        val accountAddress get() = Address.from(accountId)
        val address get() = Address.from(chatAddress)
        override fun toString() = "Route.Chat(data=${data})"
    }

    interface Sys {
        fun navigate(route: Route)
        suspend fun bind(navigator: Any)
    }

    class Navigate(private val sys: Sys) : (Route) -> Unit {
        override fun invoke(route: Route) = runBlocking { sys.navigate(route) }
    }
}
