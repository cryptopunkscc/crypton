package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.Presence

internal class StorePresenceInteractor(private val store: Presence.Store) {
    suspend operator fun invoke(presence: Presence) = store reduce {
        plus(presence.resource.address to presence)
    }
}
