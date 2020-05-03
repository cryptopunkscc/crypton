package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.Actor
import cc.cryptopunks.crypton.util.Store

internal class SaveActorStatusInteractor(
    private val store: Store<Actor.Status>
) {
    suspend operator fun invoke(status: Actor.Status) = store reduce {
        status.takeIf { status != Actor.Connected }
    }
}
