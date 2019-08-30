package cc.cryptopunks.crypton.api.entities

import android.view.Gravity
import cc.cryptopunks.crypton.util.RxBroadcast
import cc.cryptopunks.crypton.util.RxPublisher
import cc.cryptopunks.crypton.api.ApiScope
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject

data class ChatMessage(
    val id: String = "",
    val stanzaId: String = "",
    val body: String = "",
    val stamp: Date = Date(),
    val from: Jid = Jid.Empty,
    val to: Jid = Jid.Empty,
    val gravity: Int = Gravity.LEFT
) {

    val formattedDate: String get() = DATE_FORMAT.format(stamp)

    fun getChatId(userId: Jid): Jid = when (userId.withoutResource) {
        from.withoutResource -> to
        else -> from
    }

    private companion object {
        private val DATE_FORMAT = SimpleDateFormat.getDateTimeInstance()
    }

    interface Send : (Jid, String) -> Unit

    @ApiScope
    class Broadcast @Inject constructor() : RxBroadcast<ChatMessage>()

    interface Publisher : RxPublisher<ChatMessage>

    interface Filter : (ChatMessage) -> Boolean {

        class Newest(
            lastStamp: AtomicReference<Date>
        ) : Filter, (ChatMessage) -> Boolean by { message ->
            (lastStamp.get() < message.stamp).also { isNewest ->
                if (isNewest)
                    lastStamp.set(message.stamp)
            }
        } {
            @Inject
            constructor() : this(AtomicReference(Date(0)))
        }


        class HasChatJid @Inject constructor(
            jid: Jid
        ) : Filter, (ChatMessage) -> Boolean {

            private val jid = jid.withoutResource

            override fun invoke(message: ChatMessage): Boolean = with(message) {
                from.withoutResource == jid || to.withoutResource == jid
            }
        }
    }
}