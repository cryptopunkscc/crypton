package cc.cryptopunks.crypton.mock

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Presence
import cc.cryptopunks.crypton.context.Roster
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow

class MockRosterNet(
    private val state: MockState
) : Roster.Net {
    override val rosterEvents: Flow<Roster.Event> get() = state.rosterEvents.consumeAsFlow()


    override fun getContacts(): List<Address> = state
        .contacts.value.toList()

    override fun addContact(user: Address) = state {
        contacts { plus(user) }
    }

    override fun invite(address: Address) {
        throw NotImplementedError()
    }

    override fun invited(address: Address) {
        throw NotImplementedError()
    }

    override fun subscriptionStatus(address: Address): Roster.Item.Status {
        TODO("Not yet implemented")
    }

    override fun sendPresence(presence: Presence) = Unit
    override fun getCachedPresences(): List<Presence> = emptyList()
    override fun subscribe(address: Address) = Unit
    override fun iAmSubscribed(address: Address): Boolean = false
}
