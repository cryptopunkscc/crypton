package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.context.SessionScope

internal suspend fun AppScope.removeSessionScope(
    address: Address,
    onRemove: suspend SessionScope.() -> Unit
) {
    val sessionScope = sessionStore.get()[address]
    if (sessionScope != null) {
        sessionScope.onRemove()
        sessionStore { minus(address) }
    }
}
