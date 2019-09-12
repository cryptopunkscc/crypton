package cc.cryptopunks.crypton.domain.selector

import androidx.paging.DataSource
import cc.cryptopunks.crypton.entity.Conversation
import javax.inject.Inject

class ConversationDataSourceSelector @Inject constructor(
    private val dao: Conversation.Dao
) : () -> DataSource.Factory<Int, Conversation> by {
    dao.dataSourceFactory()
}