package cc.cryptopunks.crypton.repo.ormlite.dao

import cc.cryptopunks.crypton.entity.FingerprintData
import cc.cryptopunks.crypton.util.ormlite.CryptonDao
import cc.cryptopunks.crypton.util.ormlite.OrmLiteCryptonDao
import com.j256.ormlite.dao.Dao
import kotlin.coroutines.CoroutineContext

class FingerprintDao(
    read: CoroutineContext,
    write: CoroutineContext,
    dao: Dao<FingerprintData, String>,
) : FingerprintData.Dao,
    CryptonDao<FingerprintData, String> by OrmLiteCryptonDao(
        dao = dao,
        read = read,
        write = write
    )
