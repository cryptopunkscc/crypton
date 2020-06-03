package cc.cryptopunks.crypton.module

import cc.cryptopunks.crypton.context.Actor
import cc.cryptopunks.crypton.context.ChatCore
import cc.cryptopunks.crypton.context.handlerRegistry
import cc.cryptopunks.crypton.context.plus
import cc.cryptopunks.crypton.handler.handleMessageRead
import cc.cryptopunks.crypton.handler.handleSendMessage
import cc.cryptopunks.crypton.handler.subscribeLastMessage
import cc.cryptopunks.crypton.interactor.MarkMessagesAsRead
import cc.cryptopunks.crypton.interactor.SaveActorStatusInteractor
import cc.cryptopunks.crypton.selector.CanConsumeSelector
import cc.cryptopunks.crypton.selector.MessageListSelector
import cc.cryptopunks.crypton.selector.MessagePagedListFlowSelector
import cc.cryptopunks.crypton.selector.PopClipboardMessageSelector
import cc.cryptopunks.crypton.service.ChatService
import cc.cryptopunks.crypton.util.Store

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
            handlers = registry
        )
    }
}
