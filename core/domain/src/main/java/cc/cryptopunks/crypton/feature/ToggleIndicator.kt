package cc.cryptopunks.crypton.feature

import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.RootScopeTag
import cc.cryptopunks.crypton.context.indicatorSys
import cc.cryptopunks.crypton.emitter
import cc.cryptopunks.crypton.feature
import cc.cryptopunks.crypton.selector.hasAccountsFlow
import cc.cryptopunks.crypton.util.logger.log
import kotlinx.coroutines.flow.map

internal fun toggleIndicator() = feature(

    emitter = emitter(RootScopeTag) {
        hasAccountsFlow().map {
            Exec.ToggleIndicator(show = it.condition)
        }
    },

    handler = { _, (condition): Exec.ToggleIndicator ->
        indicatorSys.run {
            if (isIndicatorVisible != condition) when (condition) {
                true -> showIndicator().let { "Show" }
                false -> hideIndicator().let { "Hide" }
            }.let {
                log.d { "$it indicator" }
            }
        }
    }
)
