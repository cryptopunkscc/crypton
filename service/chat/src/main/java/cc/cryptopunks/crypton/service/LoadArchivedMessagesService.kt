package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.entity.Message
import cc.cryptopunks.crypton.entity.Session
import cc.cryptopunks.crypton.interactor.SaveMessagesInteractor
import cc.cryptopunks.crypton.selector.LatestMessageSelector
import cc.cryptopunks.crypton.util.ext.invokeOnClose
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class LoadArchivedMessagesService @Inject constructor(
    private val scope: Session.Scope,
    private val latestMessage: LatestMessageSelector,
    private val readArchivedMessages: Message.Net.ReadArchived,
    private val saveMessages: SaveMessagesInteractor
) : () -> Job {

    private val log = typedLog()

    override fun invoke(): Job = scope.launch {
        log.d("start")
        invokeOnClose { log.d("stop") }
        latestMessage()
            .run { await() }
            .let { Message.Net.ReadArchived.Query(afterUid = it?.id) }
            .let(readArchivedMessages)
            .collect { saveMessages(it) }
    }
}