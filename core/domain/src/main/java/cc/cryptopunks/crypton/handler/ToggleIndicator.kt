package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.handle

fun handleToggleIndicator() = handle { _, (condition): Exec.ToggleIndicator ->
    when (condition) {
        true -> indicatorSys.showIndicator().let { "Show" }
        false -> indicatorSys.hideIndicator().let { "Hide" }
    }.let {
        log.d("$it indicator")
    }
}
