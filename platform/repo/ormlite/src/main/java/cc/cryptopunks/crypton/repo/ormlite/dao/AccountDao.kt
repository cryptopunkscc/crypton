package cc.cryptopunks.crypton.repo.ormlite.dao

import cc.cryptopunks.crypton.entity.AccountData
import cc.cryptopunks.crypton.util.ormlite.CryptonDao
import cc.cryptopunks.crypton.util.ormlite.OrmLiteCryptonDao
import cc.cryptopunks.crypton.util.ormlite.createDao
import com.j256.ormlite.support.ConnectionSource
import kotlin.coroutines.CoroutineContext

class AccountDao(
    connection: ConnectionSource,
    read: CoroutineContext,
    write: CoroutineContext,
) : AccountData.Dao,
    CryptonDao<AccountData, String> by OrmLiteCryptonDao(
        dao = connection.createDao(),
        read = read,
        write = write
    )

