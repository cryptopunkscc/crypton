package cc.cryptopunks.crypton.mock

import cc.cryptopunks.crypton.context.Notification
import cc.cryptopunks.crypton.util.typedLog

class NotificationSys : Notification.Sys {
    private val log = typedLog()
    override fun show(notification: Notification) {
        log.d("Show $notification")
    }

    override fun cancel(notification: Notification) {
        log.d("Cancel $notification")
    }
}
