package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.RootScope
import cc.cryptopunks.crypton.context.SessionScope

internal suspend fun RootScope.removeSessionScope(
    address: Address,
    onRemove: suspend SessionScope.() -> Unit
) {
    val sessionScope = sessions[address]
    if (sessionScope != null) {
        sessionScope.onRemove()
        sessions { minus(address) }
    }
}
