package cc.cryptopunks.crypton.smack.chat

import cc.cryptopunks.crypton.entity.Address
import cc.cryptopunks.crypton.entity.Message
import cc.cryptopunks.crypton.smack.ApiQualifier
import cc.cryptopunks.crypton.smack.ApiScope
import cc.cryptopunks.crypton.smack.chatMessage
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.internal.disposables.CancellableDisposable
import io.reactivex.processors.PublishProcessor
import org.jivesoftware.smack.chat2.ChatManager
import org.jivesoftware.smack.chat2.IncomingChatMessageListener
import org.jivesoftware.smack.chat2.OutgoingChatMessageListener
import org.jxmpp.jid.impl.JidCreate
import org.reactivestreams.Subscriber
import javax.inject.Inject

@ApiScope
internal class ChatMessagePublisher @Inject constructor(
    private val chatManager: ChatManager,
    private val address: Address,
    @ApiQualifier private val disposable: CompositeDisposable
) :
    Message.Api.Publisher {

    private val processor = PublishProcessor.create<Message>()

    private val incomingListener = IncomingChatMessageListener { _, message, _ ->
        processor.onNext(
            chatMessage(
                message = message
            )
        )
    }

    private val outgoingListener = OutgoingChatMessageListener { _, message, _ ->
        processor.onNext(
            chatMessage(
                message = message.apply {
                    from = JidCreate.from(address)
                }
            )
        )
    }

    init {
        disposable.add(
            CancellableDisposable {
                chatManager.apply {
                    removeIncomingListener(incomingListener)
                    removeOutgoingListener(outgoingListener)
                }
            }
        )
        chatManager.apply {
            addIncomingListener(incomingListener)
            addOutgoingListener(outgoingListener)
        }
    }

    override fun subscribe(subscriber: Subscriber<in Message>) {
        processor.subscribe(subscriber)
    }
}