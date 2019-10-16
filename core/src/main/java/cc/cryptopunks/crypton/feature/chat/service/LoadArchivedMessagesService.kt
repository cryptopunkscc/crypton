package cc.cryptopunks.crypton.feature.chat.service

import cc.cryptopunks.crypton.api.Api
import cc.cryptopunks.crypton.api.service.ApiService
import cc.cryptopunks.crypton.entity.Message
import cc.cryptopunks.crypton.feature.chat.interactor.SaveMessagesInteractor
import cc.cryptopunks.crypton.feature.chat.selector.GetLatestMessageTimestampSelector
import cc.cryptopunks.crypton.util.ext.invokeOnClose
import cc.cryptopunks.crypton.util.log
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class LoadArchivedMessagesService @Inject constructor(
    scope: Api.Scope,
    getLatestMessageTimestamp: GetLatestMessageTimestampSelector,
    readArchivedMessages: Message.Api.ReadArchived,
    saveMessages: SaveMessagesInteractor
) : () -> Job by {
    scope.launch {
        ApiService::class.log("start")
        invokeOnClose { ApiService::class.log("stop") }
        getLatestMessageTimestamp()
            .run { Message.Api.ReadArchived.Query(since = await()) }
            .let(readArchivedMessages)
            .collect { saveMessages(it) }
    }
}