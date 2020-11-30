package cc.cryptopunks.crypton.repo.test.ormlite

import cc.cryptopunks.crypton.repo.ormlite.OrmLiteAppRepo
import cc.cryptopunks.crypton.repo.ormlite.OrmLiteSessionRepo
import cc.cryptopunks.crypton.repo.test.AccountRepoTest
import cc.cryptopunks.crypton.repo.test.ChatRepoTest
import cc.cryptopunks.crypton.repo.test.DeviceRepoTest
import cc.cryptopunks.crypton.repo.test.MessageRepoTest
import cc.cryptopunks.crypton.util.ormlite.jvm.JvmConnectionSourceFactory
import cc.cryptopunks.crypton.util.ormlite.jvm.createJdbcH2ConnectionSource
import com.j256.ormlite.logger.LocalLog
import kotlinx.coroutines.runBlocking
import org.junit.runner.RunWith
import org.junit.runners.Suite


@RunWith(Suite::class)
@Suite.SuiteClasses(
    OrmLiteAccountRepoTest::class,
    OrmLiteDeviceRepoTest::class,
    OrmLiteChatRepoTest::class,
    OrmLiteMessageRepoTest::class
)
class OrmLiteRepoTest


class OrmLiteAccountRepoTest : AccountRepoTest() {
    private val appRepo = OrmLiteAppRepo(createConnection = JvmConnectionSourceFactory(inMemory = true))
    override val repo = appRepo.accountRepo
    override fun clearDatabase() = runBlocking { appRepo.accountDao.deleteAll() }
}

class OrmLiteChatRepoTest : ChatRepoTest() {
    private val sessionRepo = OrmLiteSessionRepo(createJdbcH2ConnectionSource(inMemory = true))
    override val repo = sessionRepo.chatRepo
    override fun clearDatabase() = runBlocking { sessionRepo.chatAdapter.deleteAll() }
}

class OrmLiteMessageRepoTest : MessageRepoTest() {
    init {
        System.setProperty(LocalLog.LOCAL_LOG_LEVEL_PROPERTY, "ERROR");
    }

    private val sessionRepo = OrmLiteSessionRepo(createJdbcH2ConnectionSource(inMemory = true))
    override val repo = sessionRepo.messageRepo
    override val chatRepo = sessionRepo.chatRepo
    override fun clearDatabase() = runBlocking {
        sessionRepo.run {
            messageAdapter.deleteAll()
            chatAdapter.deleteAll()
        }
    }
}

class OrmLiteDeviceRepoTest : DeviceRepoTest() {
    private val sessionRepo = OrmLiteSessionRepo(createJdbcH2ConnectionSource(inMemory = true))
    override val repo = sessionRepo.deviceRepo
}
