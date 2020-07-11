package cc.cryptopunks.crypton.context

import kotlinx.coroutines.flow.Flow

object User {

    interface Net {
        fun getContacts(): List<Address>
        fun addContact(user: Address)
        fun invite(address: Address)
        fun invited(address: Address)
    }

    interface Repo {
        suspend fun insert(user: Address)
        suspend fun insertIfNeeded(list: List<Address>)
        fun flowListByChat(chat: Chat): Flow<List<Address>>
    }
}
