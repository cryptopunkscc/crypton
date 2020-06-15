package cc.cryptopunks.crypton.context

import androidx.paging.PagedList
import kotlinx.coroutines.flow.Flow

object Roster {

    data class Item(
        val title: String,
        val letter: Char,
        val account: Address = Address.Empty,
        val message: Message = Message.Empty,
        val presence: Presence.Status = Presence.Status.Unavailable,
        val unreadMessagesCount: Int = 0
    )

    object Service {

        data class Select(val item: Item)

        object GetItems
        data class SubscribeItems(override var enable: Boolean) : Subscription
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
