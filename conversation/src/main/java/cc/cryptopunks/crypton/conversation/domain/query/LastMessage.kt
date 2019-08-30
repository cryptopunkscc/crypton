package cc.cryptopunks.crypton.conversation.domain.query

import cc.cryptopunks.crypton.common.Schedulers
import cc.cryptopunks.crypton.common.runOn
import cc.cryptopunks.crypton.entity.Conversation
import cc.cryptopunks.crypton.entity.Message
import io.reactivex.Observable
import javax.inject.Inject

class LastMessage @Inject constructor(
    dao: Message.Dao,
    schedulers: Schedulers
) : (Conversation) -> Observable<Message> by { conversation ->
    dao.lastMessage(conversation.id).runOn(schedulers)
}