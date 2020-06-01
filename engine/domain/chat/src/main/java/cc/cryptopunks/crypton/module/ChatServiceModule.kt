package cc.cryptopunks.crypton.module

import cc.cryptopunks.crypton.context.Actor
import cc.cryptopunks.crypton.context.ChatCore
import cc.cryptopunks.crypton.context.ConnectorOutput
import cc.cryptopunks.crypton.handler.handleMessageRead
import cc.cryptopunks.crypton.handler.handleSendMessage
import cc.cryptopunks.crypton.handler.subscribeLastMessage
import cc.cryptopunks.crypton.interactor.MarkMessagesAsRead
import cc.cryptopunks.crypton.interactor.SaveActorStatusInteractor
import cc.cryptopunks.crypton.interactor.SendMessageInteractor
import cc.cryptopunks.crypton.selector.CanConsumeSelector
import cc.cryptopunks.crypton.selector.MessageListSelector
import cc.cryptopunks.crypton.selector.MessagePagedListFlowSelector
import cc.cryptopunks.crypton.selector.PopClipboardMessageSelector
import cc.cryptopunks.crypton.service.ChatService
import cc.cryptopunks.crypton.util.Store
import kotlinx.coroutines.Job
import kotlin.reflect.KClass

class ChatServiceModule(
    chatCore: ChatCore
) : ChatCore by chatCore {

    private val registry = handlerRegistry {
        with(session) {
            plus(handleSendMessage(chat))
            plus(handleMessageRead())
            plus(subscribeLastMessage())
        }
    }

    private val actorStatusStore by lazy {
        Store<Actor.Status>(Actor.Stop)
    }

    val chatService by lazy {
        ChatService(
            account = session.address,
            canConsume = CanConsumeSelector(
                store = actorStatusStore,
                chat = chat
            ),
            chat = chat,
            clipboardSys = clipboardSys,
            markMessagesAsRead = MarkMessagesAsRead(
                repo = messageRepo,
                scope = sessionScope
            ),
            messageFlow = MessagePagedListFlowSelector(
                repo = messageRepo,
                mainExecutor = mainExecutor,
                queryContext = queryContext
            ),
            messageList = MessageListSelector(
                repo = messageRepo
            ),
            popClipboardMessage = PopClipboardMessageSelector(
                store = clipboardStore
            ),
            saveActorStatus = SaveActorStatusInteractor(
                store = actorStatusStore
            ),
            sendMessage = SendMessageInteractor(
                chat = chat,
                scope = sessionScope,
                messageNet = session
            ),
            handlers = registry
        )
    }
}

fun <T : Any> handle(handle: T.(ConnectorOutput) -> Job): Handle<T> = Handler(handle)

interface Handle<T> {
    operator fun T.invoke(output: ConnectorOutput = {}): Job
}

private class Handler<T>(val handle: T.(ConnectorOutput) -> Job) : Handle<T> {
    override fun T.invoke(output: ConnectorOutput) = handle(output)
}

typealias HandlerRegistry = MutableMap<KClass<*>, Handle<*>>
typealias HandlerRegistryBuilder = MutableMap<KClass<*>, Handle<*>>

fun handlerRegistry(build: HandlerRegistryBuilder.() -> Unit): HandlerRegistry =
    mutableMapOf<KClass<*>, Handle<*>>().apply(build)

inline operator fun <reified T> HandlerRegistryBuilder.plus(handle: Handle<T>) =
    plusAssign(T::class to handle)

@Suppress("UNCHECKED_CAST")
fun HandlerRegistryBuilder.dispatch(message: Any, output: ConnectorOutput = {}): Job? =
    (get(message::class) as? Handle<Any>)?.run { message(output) }
