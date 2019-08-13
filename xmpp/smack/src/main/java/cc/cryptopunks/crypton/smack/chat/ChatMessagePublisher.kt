package cc.cryptopunks.crypton.smack.chat

import cc.cryptopunks.crypton.xmpp.XmppQualifier
import cc.cryptopunks.crypton.xmpp.XmppScope
import cc.cryptopunks.crypton.xmpp.entities.ChatMessage
import cc.cryptopunks.crypton.xmpp.entities.User
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

@XmppScope
internal class ChatMessagePublisher @Inject constructor(
    private val chatManager: ChatManager,
    private val user: User,
    @XmppQualifier private val disposable: CompositeDisposable
) :
    ChatMessage.Publisher {

    private val processor = PublishProcessor.create<ChatMessage>()

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
                    from = JidCreate.from(user.jid)
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

    override fun subscribe(subscriber: Subscriber<in ChatMessage>) {
        processor.subscribe(subscriber)
//        val incomingListener = IncomingChatMessageListener { _, message, _ ->
//            subscriber.onNext(
//                chatMessage(
//                    message = message
//                )
//            )
//        }
//
//        val outgoingListener = OutgoingChatMessageListener { _, message, _ ->
//            subscriber.onNext(
//                chatMessage(
//                    message = message.apply {
//                        from = JidCreate.from(user.jid)
//                    }
//                )
//            )
//        }
//
//        val subscription = object : Subscription {
//            override fun cancel() {
//                chatManager.apply {
//                    removeIncomingListener(incomingListener)
//                    removeOutgoingListener(outgoingListener)
//                }
//            }
//
//            override fun request(n: Long) {}
//        }
//
//        subscriber.onSubscribe(subscription)
//
//        chatManager.apply {
//            addIncomingListener(incomingListener)
//            addOutgoingListener(outgoingListener)
//        }
    }
}