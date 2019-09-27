package cc.cryptopunks.crypton.domain.selector

import cc.cryptopunks.crypton.entity.Chat
import cc.cryptopunks.crypton.entity.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject

class LastMessageSelector @Inject constructor(
    dao: Message.Dao
) : (Chat) -> Flow<Message> by { conversation ->
    dao.flowLatest(conversation.id).filterNotNull()
}