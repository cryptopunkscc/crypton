package cc.cryptopunks.crypton.fragment

import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.Person
import cc.cryptopunks.crypton.chat.R
import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Notification
import java.text.SimpleDateFormat
import java.util.*

class AndroidChatNotificationFactory(
    private val context: Application,
    private val mainActivityClass: Class<*>
) : (Notification) -> android.app.Notification {

    private val person = Person.Builder().setName("Me").build()

    private val dateFormat = SimpleDateFormat(
        "d MMM • HH:mm",
        Locale.getDefault()
    )

    override fun invoke(notification: Notification) = (notification as Notification.Messages).run {
        NotificationCompat.Builder(
            context,
            Message.Notification.channelId
        )
            .setContentTitle(chatAddress.toString())
            .setContentText(formatMessageCount())
            .setContentInfo(formatMessageCount())
            .setSubText(formatDate())
            .setWhen(timestamp)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentIntent(pendingIntent())
            .setGroup(chatAddress.id)
            .setGroupSummary(true)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setStyle(getMessageStyle())
            .build()!!
    }


    private fun Notification.Messages.formatDate() = dateFormat.format(timestamp)

    private fun Notification.Messages.formatMessageCount() = context.resources
        .getQuantityText(R.plurals.messages_count, messages.size)

    private fun pendingIntent() = PendingIntent.getActivity(context, 0, mainActivityIntent(), 0)

    private fun mainActivityIntent() = Intent(context, mainActivityClass)

    private fun Notification.Messages.getMessageStyle() = NotificationCompat.MessagingStyle(
        person
    )
        .also { style ->
            style.isGroupConversation = false

            mutableMapOf<Address, Person>().also { cache ->
                messages.forEach { message ->
                    style.addMessage(
                        message.text,
                        message.timestamp,
                        cache.getOrPut(
                            message.from.address
                        ) {
                            Person.Builder()
                                .setName(message.from.address.local.capitalize())
                                .build()
                        }
                    )
                }
            }
        }
}

private val Notification.Messages.timestamp get() = messages.last().timestamp
