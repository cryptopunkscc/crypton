package cc.cryptopunks.crypton.repo.ormlite

import cc.cryptopunks.crypton.context.Repo
import cc.cryptopunks.crypton.repo.AccountRepo
import cc.cryptopunks.crypton.repo.ClipboardRepo
import cc.cryptopunks.crypton.repo.ormlite.dao.AccountDao
import cc.cryptopunks.crypton.util.ormlite.ConnectionSourceFactory

class OrmLiteAppRepo(
    read: Repo.Context.Query = Repo.Context.Query(),
    write: Repo.Context.Transaction = Repo.Context.Transaction(),
    createConnection: ConnectionSourceFactory,
) : Repo {

    private val connection = createConnection("crypton")
    val accountDao = AccountDao(connection, read, write)

    override val accountRepo = AccountRepo(accountDao)
    override val clipboardRepo = ClipboardRepo()
    override val createSessionRepo = OrmLiteSessionRepo.Factory(createConnection, read, write)
}
