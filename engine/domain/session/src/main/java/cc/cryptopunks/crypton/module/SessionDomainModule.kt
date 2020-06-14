package cc.cryptopunks.crypton.module

import cc.cryptopunks.crypton.context.SessionCore
import cc.cryptopunks.crypton.interactor.ChatNotificationInteractor
import cc.cryptopunks.crypton.interactor.CreateChatInteractor
import cc.cryptopunks.crypton.interactor.SaveMessagesInteractor
import cc.cryptopunks.crypton.interactor.SessionEventHandler
import cc.cryptopunks.crypton.interactor.createFlushQueuedMessages
import cc.cryptopunks.crypton.selector.ApiEventSelector
import cc.cryptopunks.crypton.selector.FetchArchivedMessagesSelector
import cc.cryptopunks.crypton.selector.OmemoInitializationsSelector
import cc.cryptopunks.crypton.selector.UnreadMessagesSelector
import cc.cryptopunks.crypton.service.ChatBackgroundService
import cc.cryptopunks.crypton.service.MessageEventsSelector
import cc.cryptopunks.crypton.service.SessionService

class SessionDomainModule(
    sessionCore: SessionCore
) :
    SessionCore by sessionCore {

    val sessionService by lazy {
        SessionService(
            apiEventSelector = ApiEventSelector(session),
            sessionEventHandler = SessionEventHandler(session, networkSys)
        )
    }

    val chatBackgroundService by lazy {
        ChatBackgroundService(
            saveMessages = SaveMessagesInteractor(
                scope = session.scope,
                address = session.address,
                createChat = CreateChatInteractor(
                    repo = sessionCore.chatRepo,
                    net = session,
                    address = session.address
                ),
                messageRepo = sessionCore.messageRepo
            ),
            messageEvents = MessageEventsSelector(session),
            fetchArchivedMessages = FetchArchivedMessagesSelector(
                messageRepo = sessionCore.messageRepo,
                messageNet = session
            ),
            chatNotification = ChatNotificationInteractor(
                store = sessionCore.connectableBindingsStore,
                sys = sessionCore.notificationSys
            ),
            unreadMessages = UnreadMessagesSelector(
                messageRepo = sessionCore.messageRepo
            ),
            omemoInitializationsSelector = OmemoInitializationsSelector(session),
            flushQueuedMessages = session.createFlushQueuedMessages()
        )
    }
}
