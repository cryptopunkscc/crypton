package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.Session
import cc.cryptopunks.crypton.interactor.ChatNotificationInteractor
import cc.cryptopunks.crypton.interactor.SaveMessagesInteractor
import cc.cryptopunks.crypton.selector.FetchArchivedMessagesSelector
import cc.cryptopunks.crypton.selector.OmemoInitializationsSelector
import cc.cryptopunks.crypton.selector.UnreadMessagesSelector
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ChatBackgroundService internal constructor(
    private val unreadMessages: UnreadMessagesSelector,
    private val chatNotification: ChatNotificationInteractor,
    private val fetchArchivedMessages: FetchArchivedMessagesSelector,
    private val saveMessages: SaveMessagesInteractor,
    private val messageEvents: MessageEventsSelector,
    private val omemoInitializationsSelector: OmemoInitializationsSelector,
    private val flushQueuedMessages: () -> Job
) : Session.BackgroundService {
    private val log = typedLog()

    override suspend fun invoke() {
        log.d("Start")
        coroutineScope {
            launch { unreadMessages().collect { messages -> chatNotification(messages) } }
            launch { messageEvents().collect { event -> saveMessages(event.message) } }
            launch {
                omemoInitializationsSelector().collect {
                    flushQueuedMessages()
                    fetchArchivedMessages().collect { messages -> saveMessages(messages).join() }
                }
            }
        }
    }
}
