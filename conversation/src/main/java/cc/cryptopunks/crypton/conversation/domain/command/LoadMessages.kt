package cc.cryptopunks.crypton.conversation.domain.command

import cc.cryptopunks.crypton.common.Schedulers
import cc.cryptopunks.crypton.common.runOn
import cc.cryptopunks.crypton.core.entity.Account
import cc.cryptopunks.crypton.core.entity.Conversation
import cc.cryptopunks.crypton.core.entity.Message
import io.reactivex.Completable
import javax.inject.Inject

class LoadMessages @Inject constructor(
    messageDao: Message.Dao,
    conversationDao: Conversation.Dao,
    accountDao: Account.Dao,
    schedulers: Schedulers
) : () -> Completable by {
    //TODO: replace mock witch integration
    Completable.fromAction {
        accountDao.getAll().firstOrNull()?.run {
            (1L..200).forEach {
                conversationDao.insertIfNeeded(
                    Conversation(
                        id = it,
                        accountId = id,
                        title = "Conversation $it"
                    )
                )

                messageDao.insertOrUpdate(
                    Message(
                        id = it,
                        conversationId = it,
                        text = "message $it"
                    )
                )
                Thread.sleep(50)
            }
        }
    }.runOn(schedulers)
}