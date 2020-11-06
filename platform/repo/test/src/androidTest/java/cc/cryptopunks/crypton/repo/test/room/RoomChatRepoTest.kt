package cc.cryptopunks.crypton.repo.test.room

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import cc.cryptopunks.crypton.repo.ChatRepo
import cc.cryptopunks.crypton.repo.test.ChatRepoTest
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RoomChatRepoTest : ChatRepoTest() {
    private val db = roomTestDatabase(InstrumentationRegistry.getInstrumentation().context)
    override val repo: ChatRepo = ChatRepo(db.chatDao, db.chatUserDao, db.userDao)
    override fun clearDatabase() = db.clearAllTables()
}

