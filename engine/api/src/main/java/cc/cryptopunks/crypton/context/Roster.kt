package cc.cryptopunks.crypton.context

import androidx.paging.PagedList
import kotlinx.coroutines.flow.Flow

object Roster {

    data class Item(
        val title: String,
        val letter: Char,
        val message: Message = Message.Empty,
        val presence: Presence.Status = Presence.Status.Unavailable,
        val unreadMessagesCount: Int = 0
    )

    interface Service : Connectable {
        interface Input
        interface Output

        object GetItems : Input
        data class Items(val items: PagedList<Connectable>) : Output
    }

    interface Service2 : Connectable {
        object GetItems
        data class Items(val list: List<Item>)
    }

    interface Net {
        val rosterEvents: Flow<Event>

        interface Event

        sealed class Loading : Event {
            data class Success(val roster: Any): Loading()
            data class Failed(val exception: Exception): Loading()
        }

        data class PresenceChanged(val presence: Presence) : Event
    }
}
