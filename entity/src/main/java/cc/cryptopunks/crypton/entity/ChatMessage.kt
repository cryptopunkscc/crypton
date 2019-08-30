package cc.cryptopunks.crypton.entity

import cc.cryptopunks.crypton.util.RxPublisher
import java.util.*
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject

data class ChatMessage(
    val id: String = "",
    val stanzaId: String = "",
    val body: String = "",
    val stamp: Date = Date(),
    val from: RemoteId = RemoteId.Empty,
    val to: RemoteId = RemoteId.Empty
) {

    interface Api {
        val sendMessage: Send
        val chatMessagePublisher: Publisher

        interface Send : (RemoteId, String) -> Unit
        interface Publisher : RxPublisher<ChatMessage>
    }

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


        class HasChatId @Inject constructor(
            remoteId: RemoteId
        ) : Filter, (ChatMessage) -> Boolean {

            private val jid = remoteId.withoutResource

            override fun invoke(message: ChatMessage): Boolean = with(message) {
                from.withoutResource == jid || to.withoutResource == jid
            }
        }
    }
}