package cc.cryptopunks.crypton.context

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
