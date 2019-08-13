package cc.cryptopunks.crypton.xmpp.entities

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