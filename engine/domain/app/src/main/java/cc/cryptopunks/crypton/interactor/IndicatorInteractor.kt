package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.Indicator

internal class IndicatorInteractor(
    private val sys: Indicator.Sys
) : (Boolean) -> Unit by { show ->
    when (show) {
        true -> sys.showIndicator()
        false -> sys.hideIndicator()
    }
}
