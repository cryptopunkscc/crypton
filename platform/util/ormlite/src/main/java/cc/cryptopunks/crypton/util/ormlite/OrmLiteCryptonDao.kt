package cc.cryptopunks.crypton.util.ormlite

import androidx.paging.DataSource
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.table.TableUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class OrmLiteCryptonDao<T: Any, ID>(
    private val dao: Dao<T, ID>,
    private val read: CoroutineContext,
    private val write: CoroutineContext,
) : CryptonDao<T, ID> {
    override suspend fun insert(entity: T) {
        withContext(write) { dao.create(entity) }
    }

    override suspend fun insert(list: List<T>) {
        withContext(write) { dao.create(list) }
    }

    override suspend fun insertIfNeeded(entity: T) {
        withContext(write) { dao.createIfNotExists(entity) }
    }

    override suspend fun insertIfNeeded(list: List<T>) {
        withContext(write) {
            dao.callBatchTasks {
                list.forEach(dao::createIfNotExists)
            }
        }
    }

    override suspend fun insertOrUpdate(entity: T) {
        withContext(write) { dao.createOrUpdate(entity) }
    }

    override suspend fun insertOrUpdate(list: List<T>) {
        withContext(write) {
            dao.callBatchTasks {
                list.forEach(dao::createOrUpdate)
            }
        }
    }

    override suspend fun get(id: ID): T? = withContext(read) {
        dao.queryForId(id)
    }

    override suspend fun contains(id: ID): ID? =
        id.takeIf(dao::idExists)

    override suspend fun delete(id: ID) {
        withContext(write) { dao.deleteById(id) }
    }

    override suspend fun delete(ids: List<ID>) {
        withContext(write) { dao.deleteIds(ids) }
    }

    override suspend fun deleteAll() {
        withContext(write) {
            TableUtils.dropTable(dao, false)
            TableUtils.createTable(dao)
        }
    }

    override suspend fun update(data: T) {
        withContext(write) { dao.update(data) }
    }

    override suspend fun list(): List<T> {
        return withContext(read) { dao.queryForAll() }
    }

    override fun flowList(): Flow<List<T>> =
        dao.changesFlow { send(list()) }

    override fun dataSourceFactory(): DataSource.Factory<Int, T> =
        DaoPositionalDataSource.Factory(dao)
}
