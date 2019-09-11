package cc.cryptopunks.crypton.entity

data class Contact(
    val remoteId: RemoteId,
    val name: String
) {

    interface Add : (Contact) -> Unit
    interface Remove : (Contact) -> Unit
}