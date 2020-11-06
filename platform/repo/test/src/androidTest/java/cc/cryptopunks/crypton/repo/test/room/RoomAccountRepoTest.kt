package cc.cryptopunks.crypton.repo.test.room

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import cc.cryptopunks.crypton.repo.AccountRepo
import cc.cryptopunks.crypton.repo.test.AccountRepoTest
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RoomAccountRepoTest : AccountRepoTest() {
    private val db = roomTestDatabase(InstrumentationRegistry.getInstrumentation().context)
    override val repo: AccountRepo = AccountRepo(db.accountDao)
    override fun clearDatabase() = db.clearAllTables()
}
