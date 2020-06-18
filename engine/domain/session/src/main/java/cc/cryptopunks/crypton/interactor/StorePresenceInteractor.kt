package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.context.Presence

internal suspend fun AppScope.storePresence(presence: Presence) = presenceStore reduce {
    plus(presence.resource.address to presence)
}

