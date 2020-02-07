package cc.cryptopunks.crypton.context

import androidx.paging.PagedList

object Roster {

    object Item {

        data class State(
            val title: String,
            val letter: Char,
            val message: Message = Message.Empty,
            val presence: Presence.Status = Presence.Status.Unavailable,
            val unreadMessagesCount: Int = 0
        ) : Service.Output

        interface Service : Connectable {
            interface Output
        }
    }

    interface Service : Connectable {
        interface Input
        interface Output

        object GetItems: Input
        data class Items(val items: PagedList<Connectable>) : Output
    }
}