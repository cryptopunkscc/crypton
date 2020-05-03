package cc.cryptopunks.crypton.context

sealed class Route(
    open val data: MutableMap<String, Any?> = mutableMapOf()
) {
    class Raw(val id: Int) : Route()
    object Dashboard : Route()
    object Roster : Route()
    object SetAccount : Route()
    object Login : Route()
    object Register : Route()
    object AccountList : Route()
    object AccountManagement : Route()
    object CreateChat : Route()
    class Chat(data: MutableMap<String, Any?> = mutableMapOf()) : Route(data) {
        var accountId: String by data
        var chatAddress: String by data
        val address get() = Address.from(chatAddress)
    }

    interface Sys {
        fun navigate(route: Route)
        suspend fun bind(navigator: Any)
    }

    class Navigate(private val sys: Sys) : (Route) -> Unit {
        override fun invoke(route: Route) = sys.navigate(route)
    }
}
