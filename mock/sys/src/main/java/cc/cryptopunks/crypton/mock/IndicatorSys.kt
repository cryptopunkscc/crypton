package cc.cryptopunks.crypton.mock

import cc.cryptopunks.crypton.context.Indicator
import cc.cryptopunks.crypton.util.typedLog

class IndicatorSys : Indicator.Sys {
    private val log = typedLog()
    override fun showIndicator() {
        log.d("Show indicator")
    }

    override fun hideIndicator() {
        log.d("Hide indicator")
    }
}
