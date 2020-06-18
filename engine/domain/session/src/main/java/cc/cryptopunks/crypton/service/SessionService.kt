package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.context.collect
import cc.cryptopunks.crypton.handler.handleApiEvent
import cc.cryptopunks.crypton.handler.handleOmemoInitialized
import cc.cryptopunks.crypton.handler.handlePresenceChange
import cc.cryptopunks.crypton.interactor.SaveMessagesInteractor
import cc.cryptopunks.crypton.interactor.StorePresenceInteractor
import cc.cryptopunks.crypton.interactor.UpdateChatNotificationInteractor
import cc.cryptopunks.crypton.selector.ApiEventSelector
import cc.cryptopunks.crypton.selector.FetchArchivedMessagesSelector
import cc.cryptopunks.crypton.selector.OmemoInitializationsSelector
import cc.cryptopunks.crypton.selector.PresenceChangedFlowSelector
import cc.cryptopunks.crypton.selector.UnreadMessagesSelector
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SessionService(
    sessionScope: SessionScope
) :
    SessionScope by sessionScope,
    SessionScope.BackgroundService {

    override val log = typedLog()

    override suspend fun invoke() = coroutineScope {
        log.d("Start")
        launch { apiEventSelector().collect(handleApiEvent) }
        launch { omemoInitializationsSelector().collect(handleOmemoInitialized) }
        launch { unreadMessages().collect { messages -> updateChatNotification(messages) } }
        launch { messageEvents().collect { event -> saveMessages(event.message) } }
        launch { presenceChangedFlow().collect(handlePresenceChanged) }
        Unit
    }

    private val apiEventSelector = ApiEventSelector(sessionScope)

    private val handleApiEvent = handleApiEvent(networkSys)


    private val omemoInitializationsSelector by lazy { OmemoInitializationsSelector(sessionScope) }

    private val handleOmemoInitialized by lazy {
        handleOmemoInitialized(
            fetchArchivedMessages = FetchArchivedMessagesSelector(messageRepo, sessionScope),
            saveMessages = saveMessages
        )
    }


    private val unreadMessages by lazy { UnreadMessagesSelector(sessionScope.messageRepo) }

    private val updateChatNotification by lazy {
        UpdateChatNotificationInteractor(
            sys = notificationSys,
            store = connectableBindingsStore
        )
    }


    private val saveMessages by lazy { SaveMessagesInteractor(sessionScope) }


    private val presenceChangedFlow = PresenceChangedFlowSelector(sessionScope)

    private val handlePresenceChanged by lazy {
        handlePresenceChange(StorePresenceInteractor(presenceStore))
    }
}
