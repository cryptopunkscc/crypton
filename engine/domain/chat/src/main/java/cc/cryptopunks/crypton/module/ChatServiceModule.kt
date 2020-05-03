package cc.cryptopunks.crypton.module

import cc.cryptopunks.crypton.context.Actor
import cc.cryptopunks.crypton.context.ChatCore
import cc.cryptopunks.crypton.interactor.MarkMessagesAsRead
import cc.cryptopunks.crypton.interactor.SaveActorStatusInteractor
import cc.cryptopunks.crypton.interactor.SendMessageInteractor
import cc.cryptopunks.crypton.selector.CanConsumeSelector
import cc.cryptopunks.crypton.selector.MessagePagedListSelector
import cc.cryptopunks.crypton.selector.PopClipboardMessageSelector
import cc.cryptopunks.crypton.service.ChatService
import cc.cryptopunks.crypton.util.Store

class ChatServiceModule(
    chatCore: ChatCore
) : ChatCore by chatCore {

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
            messageFlow = MessagePagedListSelector(
                repo = messageRepo,
                mainExecutor = mainExecutor,
                queryContext = queryContext
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
            )
        )
    }
}
