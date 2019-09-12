package cc.cryptopunks.crypton.domain.selector

import cc.cryptopunks.crypton.entity.Conversation
import cc.cryptopunks.crypton.entity.Message
import cc.cryptopunks.crypton.util.Schedulers
import cc.cryptopunks.crypton.util.runOn
import io.reactivex.Flowable
import javax.inject.Inject

class LastMessageSelector @Inject constructor(
    dao: Message.Dao,
    schedulers: Schedulers
) : (Conversation) -> Flowable<Message> by { conversation ->
    dao.flowableLatest(conversation.id).runOn(schedulers)
}