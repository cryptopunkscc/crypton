package cc.cryptopunks.crypton.domain.query

import cc.cryptopunks.crypton.entity.Conversation
import cc.cryptopunks.crypton.entity.Message
import cc.cryptopunks.crypton.util.Schedulers
import cc.cryptopunks.crypton.util.runOn
import io.reactivex.Flowable
import javax.inject.Inject

class LastMessage @Inject constructor(
    dao: Message.Dao,
    schedulers: Schedulers
) : (Conversation) -> Flowable<Message> by { conversation ->
    dao.flowableLatest(conversation.id).runOn(schedulers)
}