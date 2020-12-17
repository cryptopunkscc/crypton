package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.Presence
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.context.presenceStore

internal suspend fun SessionScope.storePresence(presence: Presence) = presenceStore reduce {
    plus(presence.resource.address to presence)
}

