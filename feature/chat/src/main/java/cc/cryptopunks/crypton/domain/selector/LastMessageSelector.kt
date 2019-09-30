package cc.cryptopunks.crypton.domain.selector

import cc.cryptopunks.crypton.entity.Chat
import cc.cryptopunks.crypton.entity.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject

class LastMessageSelector @Inject constructor(
    repo: Message.Repo
) : (Chat) -> Flow<Message> by { chat ->
    repo.flowLatest(chat.id).filterNotNull()
}