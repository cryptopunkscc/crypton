package cc.cryptopunks.crypton.mock.net

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.User
import cc.cryptopunks.crypton.mock.MockState
import cc.cryptopunks.crypton.mock.invoke

class UserNetMock(
    private val state: MockState
) : User.Net {

    override val getContacts = object : User.Net.GetContacts {
        override fun invoke(): List<User> = state.contacts.value.toList()
    }
    override val addContact = object : User.Net.AddContact {
        override fun invoke(user: User): Unit = state {
            contacts { plus(user) }
        }

    }
    override val invite = object : User.Net.Invite {
        override fun invoke(user: Address) {
            throw NotImplementedError()
        }
    }
    override val invited = object : User.Net.Invited {
        override fun invoke(address: Address) {
            throw NotImplementedError()
        }
    }
}
