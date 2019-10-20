package cc.cryptopunks.crypton.entity

import kotlinx.coroutines.flow.Flow

data class User(
    val address: Address = Address.Empty
) {

    constructor(string: String) : this(
        address = Address.from(string)
    )

    interface Net {
        val getContacts: GetContacts
        val addContact: AddContact
        val invite: Invite
        val invited: Invited

        interface GetContacts : () -> List<User>
        interface AddContact : (User) -> Unit
        interface Invite : (Address) -> Unit
        interface Invited : (Address) -> Unit
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