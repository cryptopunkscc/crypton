package cc.cryptopunks.crypton.repo.ormlite

import cc.cryptopunks.crypton.context.Repo
import cc.cryptopunks.crypton.repo.AccountRepo
import cc.cryptopunks.crypton.repo.ClipboardRepo
import cc.cryptopunks.crypton.repo.ormlite.dao.AccountDao
import com.j256.ormlite.support.ConnectionSource

class OrmLiteAppRepo(
    connection: ConnectionSource,
    read: Repo.Context.Query = Repo.Context.Query(),
    write: Repo.Context.Transaction = Repo.Context.Transaction(),
) : Repo {

    override val accountRepo = AccountRepo(AccountDao(connection, read, write))
    override val clipboardRepo = ClipboardRepo()
    override val createSessionRepo = OrmLiteSessionRepo.Factory(connection, read, write)
}
