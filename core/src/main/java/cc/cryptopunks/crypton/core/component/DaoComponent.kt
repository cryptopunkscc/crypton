package cc.cryptopunks.crypton.core.component

import cc.cryptopunks.crypton.core.entity.Account
import cc.cryptopunks.crypton.core.entity.Conversation
import cc.cryptopunks.crypton.core.entity.Message

interface DaoComponent {
    val accountDao: Account.Dao
    val conversationDao: Conversation.Dao
    val messageDao: Message.Dao
}