package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.dep

val RootScope.indicatorSys: Indicator.Sys by dep()

object Indicator {
    const val serviceName = "Indicator Service"

    object Notification {
        val id = hashCode()
        const val channelId = "Indicator"
    }

    interface Sys {
        val isIndicatorVisible: Boolean
        fun showIndicator()
        fun hideIndicator()
    }
}
