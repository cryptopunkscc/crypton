package cc.cryptopunks.crypton.entity

data class UserPresence(
    val address: Address,
    val presence: Presence
) {
    interface Net {
        val getCached: GetCached
        interface GetCached : () -> List<UserPresence>
    }
}