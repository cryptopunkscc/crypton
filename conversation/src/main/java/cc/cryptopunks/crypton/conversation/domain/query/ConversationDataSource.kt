package cc.cryptopunks.crypton.conversation.domain.query

import androidx.paging.DataSource
import cc.cryptopunks.crypton.entity.Conversation
import javax.inject.Inject

class ConversationDataSource @Inject constructor(
    private val dao: Conversation.Dao
) : () -> DataSource.Factory<Int, Conversation> by {
    dao.getAllPaged()
}