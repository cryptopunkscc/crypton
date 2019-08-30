package cc.cryptopunks.crypton.entity

data class User(
    val remoteId: RemoteId = RemoteId.Empty
) {

    interface Api {
        val getContacts: GetContacts
        val invite: Invite
        val invited: Invited

        interface GetContacts: () -> List<User>

        interface Invite : (User) -> Unit
        interface Invited: (User) -> Unit

    }

    companion object {
        val Empty = User()
    }
}