package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.entity.Message
import cc.cryptopunks.crypton.entity.Session
import cc.cryptopunks.crypton.interactor.SaveMessagesInteractor
import cc.cryptopunks.crypton.selector.GetLatestMessageTimestampSelector
import cc.cryptopunks.crypton.util.ext.invokeOnClose
import cc.cryptopunks.crypton.util.log
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class LoadArchivedMessagesService @Inject constructor(
    scope: Session.Scope,
    getLatestMessageTimestamp: GetLatestMessageTimestampSelector,
    readArchivedMessages: Message.Net.ReadArchived,
    saveMessages: SaveMessagesInteractor
) : () -> Job by {
    scope.launch {
        LoadArchivedMessagesService::class.log("start")
        invokeOnClose { LoadArchivedMessagesService::class.log("stop") }
        getLatestMessageTimestamp()
            .run { Message.Net.ReadArchived.Query(since = await()) }
            .let(readArchivedMessages)
            .collect { saveMessages(it) }
    }
}