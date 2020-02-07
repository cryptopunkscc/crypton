package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.annotation.SessionScope
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Session
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@SessionScope
class ChatSyncService @Inject constructor(
    private val scope: Session.Scope,
    private val chatList: Chat.Net.MultiUserChatList,
    private val chatFlow: Chat.Net.MultiUserChatFlow,
    private val chatRepo: Chat.Repo
) : () -> Job {

    private val log = typedLog()

    override fun invoke(): Job = scope.launch {
        chatList().forEach {
            log.e("it")
//            chatRepo.insertIfNeeded(it) TODO
        }
        chatFlow.collect {
            log.e("it")

//            chatRepo.insertIfNeeded(it) TODO
        }
    }
}