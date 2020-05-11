package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.Indicator
import cc.cryptopunks.crypton.util.typedLog

internal class IndicatorInteractor(
    private val sys: Indicator.Sys
) : (Boolean) -> Unit {
    private val log = typedLog()
    override fun invoke(show: Boolean) {
        when (show) {
            true -> sys.showIndicator().let { "Show" }
            false -> sys.hideIndicator().let { "Hide" }
        }.let {
            log.d("$it indicator")
        }
    }
}
