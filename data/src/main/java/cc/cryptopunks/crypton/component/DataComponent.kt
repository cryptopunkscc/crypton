package cc.cryptopunks.crypton.component

import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.entity.Conversation
import cc.cryptopunks.crypton.entity.Message

interface DataComponent {
    val accountDao: Account.Dao
    val conversationDao: Conversation.Dao
    val messageDao: Message.Dao
}