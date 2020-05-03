package cc.cryptopunks.crypton.context

object Indicator {
    const val serviceName = "Indicator Service"

    object Notification {
        val id = hashCode()
        const val channelId = "Indicator"
    }

    interface Sys {
        fun showIndicator()
        fun hideIndicator()
    }
}
