package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Net
import cc.cryptopunks.crypton.context.Session
import cc.cryptopunks.crypton.context.handle
import cc.cryptopunks.crypton.interactor.SaveMessagesInteractor
import cc.cryptopunks.crypton.interactor.flushQueuedMessages
import cc.cryptopunks.crypton.selector.FetchArchivedMessagesSelector
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

internal fun Session.handleOmemoInitialized(
    fetchArchivedMessages: FetchArchivedMessagesSelector,
    saveMessages: SaveMessagesInteractor
) = handle<Net.OmemoInitialized> {
    scope.launch {
        launch { flushQueuedMessages() }
        launch { fetchArchivedMessages().collect { messages -> saveMessages(messages).join() } }
    }
}
