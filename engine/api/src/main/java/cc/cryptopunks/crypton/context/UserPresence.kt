package cc.cryptopunks.crypton.context

data class UserPresence(
    val address: Address,
    val presence: Presence
) {
    interface Net {
        val getCached: GetCached
        interface GetCached : () -> List<UserPresence>
    }
}