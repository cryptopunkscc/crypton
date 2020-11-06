package cc.cryptopunks.crypton.repo.ormlite

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Repo
import cc.cryptopunks.crypton.context.SessionRepo
import cc.cryptopunks.crypton.entity.ChatData
import cc.cryptopunks.crypton.entity.ChatUserData
import cc.cryptopunks.crypton.entity.FingerprintData
import cc.cryptopunks.crypton.entity.MessageData
import cc.cryptopunks.crypton.entity.UserData
import cc.cryptopunks.crypton.util.ormlite.createDao
import cc.cryptopunks.crypton.repo.ChatRepo
import cc.cryptopunks.crypton.repo.DeviceRepo
import cc.cryptopunks.crypton.repo.MessageRepo
import cc.cryptopunks.crypton.repo.RosterRepo
import cc.cryptopunks.crypton.repo.ormlite.dao.ChatDao
import cc.cryptopunks.crypton.repo.ormlite.dao.ChatUserDao
import cc.cryptopunks.crypton.repo.ormlite.dao.FingerprintDao
import cc.cryptopunks.crypton.repo.ormlite.dao.MessageDao
import cc.cryptopunks.crypton.repo.ormlite.dao.UserDao
import cc.cryptopunks.crypton.util.ormlite.ConnectionSourceFactory
import com.j256.ormlite.support.ConnectionSource

class OrmLiteSessionRepo(
    private val connection: ConnectionSource,
    override val queryContext: Repo.Context.Query = Repo.Context.Query(),
    override val transactionContext: Repo.Context.Transaction = Repo.Context.Transaction(),
) : SessionRepo {

    private val read get() = queryContext
    private val write get() = transactionContext

    private val chatDao = connection.createDao<ChatData, String>()
    private val userDao = connection.createDao<UserData, String>()
    private val chatUserDao = connection.createDao<ChatUserData, String>()
    private val messageDao = connection.createDao<MessageData, String>()
    private val fingerprintDao = connection.createDao<FingerprintData, String>()

    val chatAdapter = ChatDao(read, write, chatDao)
    val chatUserAdapter = ChatUserDao(read, write, chatUserDao)
    val userAdapter = UserDao(read, write, userDao, chatUserDao)
    val messageAdapter = MessageDao(read, write, messageDao)
    val fingerprintAdapter = FingerprintDao(read, write, fingerprintDao)

    override val chatRepo = ChatRepo(chatAdapter, chatUserAdapter, userAdapter)
    override val messageRepo = MessageRepo(messageAdapter, read)
    override val rosterRepo = RosterRepo(userAdapter)
    override val deviceRepo = DeviceRepo(fingerprintAdapter)

    class Factory(
        private val createConnection: ConnectionSourceFactory,
        private val read: Repo.Context.Query,
        private val write: Repo.Context.Transaction,
    ) : SessionRepo.Factory {
        override fun invoke(
            address: Address,
        ): SessionRepo = OrmLiteSessionRepo(
            connection = createConnection(address.toString()),
            queryContext = read,
            transactionContext = write
        )
    }
}
