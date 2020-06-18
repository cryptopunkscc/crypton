package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.AppScope

internal fun AppScope.setIndicator(enabled: Boolean) {
    when (enabled) {
        true -> indicatorSys.showIndicator().let { "Show" }
        false -> indicatorSys.hideIndicator().let { "Hide" }
    }.let {
        log.d("$it indicator")
    }
}
