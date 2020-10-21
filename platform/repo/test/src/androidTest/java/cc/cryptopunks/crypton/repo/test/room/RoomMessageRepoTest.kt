package cc.cryptopunks.crypton.repo.test.room

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import cc.cryptopunks.crypton.repo.ChatRepo
import cc.cryptopunks.crypton.repo.MessageRepo
import cc.cryptopunks.crypton.repo.test.MessageRepoTest
import kotlinx.coroutines.Dispatchers
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RoomMessageRepoTest : MessageRepoTest() {
    private val db = roomTestDatabase(InstrumentationRegistry.getInstrumentation().context)
    override val repo = MessageRepo(db.messageDao, Dispatchers.Unconfined)
    override val chatRepo = ChatRepo(db.chatDao, db.chatUserDao, db.userDao)

    override fun clearDatabase() = db.clearAllTables()
}
