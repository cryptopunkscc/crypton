package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.Scope
import cc.cryptopunks.crypton.dep
import cc.cryptopunks.crypton.util.OpenStore
import kotlinx.coroutines.flow.Flow

val Scope.rosterItems: Roster.Items.Store by dep()
val SessionScope.rosterNet: Roster.Net by dep()

object Roster {

    data class Subscription(
        val address: Address,
        val status: Item.Status
    )

    data class Item(
        val title: String,
        val letter: Char,
        val chatAddress: Address = Address.Empty,
        val account: Address = Address.Empty,
        val message: Message = Message.Empty,
        val presence: Presence.Status = Presence.Status.Unavailable,
        val unreadMessagesCount: Int = 0,
        val updatedAt: Long = 0
    ) {
        enum class Status(val symbol: Char) {
            /**
             * The user does not have a subscription to the contact's presence, and the contact does not
             * have a subscription to the user's presence; this is the default value, so if the
             * subscription attribute is not included then the state is to be understood as "none".
             */
            none('⊥'),

            /**
             * The user has a subscription to the contact's presence, but the contact does not have a
             * subscription to the user's presence.
             */
            to('←'),

            /**
             * The contact has a subscription to the user's presence, but the user does not have a
             * subscription to the contact's presence.
             */
            from('→'),

            /**
             * The user and the contact have subscriptions to each other's presence (also called a
             * "mutual subscription").
             */
            both('↔'),

            /**
             * The user wishes to stop receiving presence updates from the subscriber.
             */
            remove('⚡'),
        }
    }

    data class Items(val list: List<Item>) {
        class Store: OpenStore<Items>(Items(emptyList()))
    }


    interface Event

    sealed class Loading : Event {
        data class Success(val roster: Any) : Loading()
        data class Failed(val exception: Exception) : Loading()
    }

    data class PresenceChanged(val presence: Presence) : Event

    interface Net {
        fun getContacts(): List<Address>
        fun addContact(user: Address)
        fun invite(address: Address)
        fun invited(address: Address)
        fun subscriptionStatus(address: Address) : Item.Status
        fun subscribe(address: Address)
        fun iAmSubscribed(address: Address): Boolean
        fun sendPresence(presence: Presence)
        fun getCachedPresences(): List<Presence>
        val rosterEvents: Flow<Event>
    }

    interface Repo {
        suspend fun insert(user: Address)
        suspend fun insertIfNeeded(list: List<Address>)
        fun flowListByChat(chat: Chat): Flow<List<Address>>
    }
}
