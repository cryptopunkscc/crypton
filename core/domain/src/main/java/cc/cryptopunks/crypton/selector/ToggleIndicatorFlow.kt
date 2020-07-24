package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.RootScope
import kotlinx.coroutines.flow.map

fun RootScope.toggleIndicatorFlow() = hasAccountsFlow().map {
    Exec.ToggleIndicator(show = it.condition)
}
