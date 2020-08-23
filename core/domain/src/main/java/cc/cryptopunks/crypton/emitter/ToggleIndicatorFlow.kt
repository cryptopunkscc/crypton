package cc.cryptopunks.crypton.emitter

import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.RootScope
import cc.cryptopunks.crypton.selector.hasAccountsFlow
import kotlinx.coroutines.flow.map

fun RootScope.toggleIndicatorFlow() = hasAccountsFlow().map {
    Exec.ToggleIndicator(show = it.condition)
}
