package cc.cryptopunks.crypton.mock

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.User

class MockUserNet(
    private val state: MockState
) : User.Net {

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
}
