package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.UserPresence

internal class StoreUserPresenceInteractor(private val store: UserPresence.Store) {
    suspend operator fun invoke(userPresence: UserPresence) = store reduce {
        plus(userPresence.address to userPresence.presence)
    }
}
