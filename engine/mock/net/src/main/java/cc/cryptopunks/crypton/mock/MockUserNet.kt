package cc.cryptopunks.crypton.mock

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.User

class MockUserNet(
    private val state: MockState
) : User.Net {

    override fun getContacts(): List<User> = state
        .contacts.value.toList()

    override fun addContact(user: User) = state {
        contacts { plus(user) }
    }

    override fun invite(address: Address) {
        throw NotImplementedError()
    }

    override fun invited(address: Address) {
        throw NotImplementedError()
    }
}
