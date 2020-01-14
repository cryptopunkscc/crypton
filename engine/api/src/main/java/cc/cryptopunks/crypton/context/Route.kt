package cc.cryptopunks.crypton.context

import kotlinx.coroutines.flow.Flow

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
    class Chat(
        data: MutableMap<String, Any?> = mutableMapOf()
    ) : Route(data) {
        var accountId: String by data
        var chatAddress: String by data
        val address get() = Address.from(chatAddress)
    }

    interface Api {
        val navigate: Navigate
        val navigationOutput: Output

        interface Navigate : (Route) -> Unit
        interface Output : Flow<Route>
    }
}