package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.handle
import cc.cryptopunks.crypton.util.logger.log

fun handleToggleIndicator() = handle { _, (condition): Exec.ToggleIndicator ->
    indicatorSys.run {
        if (isIndicatorVisible != condition) when (condition) {
            true -> showIndicator().let { "Show" }
            false -> hideIndicator().let { "Hide" }
        }.let {
            log.d { "$it indicator" }
        }
    }
}
