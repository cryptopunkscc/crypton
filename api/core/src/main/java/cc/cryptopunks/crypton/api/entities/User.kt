package cc.cryptopunks.crypton.api.entities

data class User(
    val jid: Jid = Jid.Empty
) {

    interface GetContacts: () -> List<User>

    interface Invite : (User) -> Unit
    interface Invited: (User) -> Unit

    companion object {
        val Empty = User()
    }
}