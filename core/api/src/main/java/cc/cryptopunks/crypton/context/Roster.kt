package cc.cryptopunks.crypton.context

import kotlinx.coroutines.flow.Flow

object Roster {

    data class Item(
        val title: String,
        val letter: Char,
        val chatAddress: Address = Address.Empty,
        val account: Address = Address.Empty,
        val message: Message = Message.Empty,
        val presence: Presence.Status = Presence.Status.Unavailable,
        val unreadMessagesCount: Int = 0
    )

    object Service {

        // Result
        data class Items(val list: List<Item>)
    }

    interface Net {
        fun getContacts(): List<Address>
        fun addContact(user: Address)
        fun invite(address: Address)
        fun invited(address: Address)


        val rosterEvents: Flow<Event>

        interface Event

        sealed class Loading : Event {
            data class Success(val roster: Any) : Loading()
            data class Failed(val exception: Exception) : Loading()
        }

        data class PresenceChanged(val presence: Presence) : Event, Account.Action
    }

    interface Repo {
        suspend fun insert(user: Address)
        suspend fun insertIfNeeded(list: List<Address>)
        fun flowListByChat(chat: Chat): Flow<List<Address>>
    }
}
