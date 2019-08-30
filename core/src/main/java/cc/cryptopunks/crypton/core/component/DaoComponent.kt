package cc.cryptopunks.crypton.core.component

import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.entity.Conversation
import cc.cryptopunks.crypton.entity.Message

interface DaoComponent {
    val accountDao: Account.Dao
    val conversationDao: Conversation.Dao
    val messageDao: Message.Dao
}