package cc.cryptopunks.crypton.mock

import cc.cryptopunks.crypton.context.Indicator

class MockIndicatorSys : Indicator.Sys {

    override val isIndicatorVisible: Boolean = true

    override fun showIndicator() {
    }

    override fun hideIndicator() {
    }
}
