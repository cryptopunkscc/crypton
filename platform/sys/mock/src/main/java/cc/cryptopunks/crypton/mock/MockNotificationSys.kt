package cc.cryptopunks.crypton.mock

import cc.cryptopunks.crypton.context.Notification

class MockNotificationSys : Notification.Sys {
    override fun show(notification: Notification) {
    }

    override fun cancel(notification: Notification) {
    }
}
