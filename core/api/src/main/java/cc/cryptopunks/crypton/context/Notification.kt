package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.dep

val RootScope.notificationSys: Notification.Sys by dep()

sealed class Notification(
    val id: Int
) {
    data class Messages(
        val destination: Int,
        val account: Address,
        val chatAddress: Address,
        val messages: List<Message>
    ) : Notification(chatAddress.hashCode())

    interface Sys {
        fun show(notification: Notification)
        fun cancel(notification: Notification)
    }
}
