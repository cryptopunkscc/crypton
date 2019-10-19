package cc.cryptopunks.crypton.entity

object Indicator {
    const val serviceName = "Indicator Service"

    object Notification {
        val id = hashCode()
        const val channelId = "Indicator channel"
    }

    interface Sys {
        val showIndicator: Show
        val hideIndicator: Hide

        interface Show : () -> Unit
        interface Hide : () -> Unit
    }
}