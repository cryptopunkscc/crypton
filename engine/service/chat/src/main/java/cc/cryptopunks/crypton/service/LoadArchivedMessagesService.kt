package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.annotation.SessionScope
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Session
import cc.cryptopunks.crypton.interactor.SaveMessagesInteractor
import cc.cryptopunks.crypton.selector.LatestMessageSelector
import cc.cryptopunks.crypton.util.ext.invokeOnClose
import cc.cryptopunks.crypton.util.ext.map
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flattenMerge
import javax.inject.Inject

@SessionScope
class LoadArchivedMessagesService @Inject constructor(
    private val scope: Session.Scope,
    private val latestMessage: LatestMessageSelector,
    private val readArchivedMessages: Message.Net.ReadArchived,
    private val multiUserChats: Chat.Net.MultiUserChatList,
    private val saveMessages: SaveMessagesInteractor
) : () -> Job {

    private val log = typedLog()

    override fun invoke(): Job = scope.launch {
        log.d("start")
        invokeOnClose { log.d("stop") }
        prepareQueries(
            since = latestMessage().await()?.timestamp
        )
            .asFlow()
            .map { readArchivedMessages(it) }
            .flattenMerge()
            .collect { saveMessages(it) }
    }

    private fun prepareQueries(
        since: Long?
    ): List<Message.Net.ReadArchived.Query> =
        Message.Net.ReadArchived.Query(since).let { query ->
            listOf(query)
//            + multiUserChats().map { chat ->
//                query.copy(chat = chat)
//            }
        }
}