package cc.cryptopunks.crypton.context

import kotlinx.coroutines.flow.Flow

data class User(
    val address: Address = Address.Empty
) {

    constructor(string: String) : this(
        address = Address.from(string)
    )

    interface Net {
        fun getContacts(): List<User>
        fun addContact(user: User)
        fun invite(address: Address)
        fun invited(address: Address)

    }

    interface Repo {
        suspend fun insert(user: User)
        suspend fun insertIfNeeded(list: List<User>)
        fun flowListByChat(chat: Chat): Flow<List<User>>
    }

    companion object {
        val Empty = User()
    }
}
