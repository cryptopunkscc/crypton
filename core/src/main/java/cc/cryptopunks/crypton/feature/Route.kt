package cc.cryptopunks.crypton.feature

sealed class Route {
    class Raw(val id: Int) : Route()
    object Dashboard : Route()
    object Roster : Route()
    object SetAccount : Route()
    object Login : Route()
    object Register : Route()
    object AccountList : Route()
    object AccountManagement : Route()
    object CreateChat : Route()
    data class Chat(val id: Long = Last) : Route() {
        companion object {
            const val Last = -1L
        }
    }
}