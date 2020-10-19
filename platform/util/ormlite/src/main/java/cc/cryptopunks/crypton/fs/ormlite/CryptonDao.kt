package cc.cryptopunks.crypton.fs.ormlite

import androidx.paging.DataSource
import kotlinx.coroutines.flow.Flow

interface CryptonDao<T, ID> {

    suspend fun insert(entity: T)
    suspend fun insert(list: List<T>)
    suspend fun insertIfNeeded(entity: T)
    suspend fun insertIfNeeded(list: List<T>)
    suspend fun insertOrUpdate(entity: T)
    suspend fun insertOrUpdate(list: List<T>)
    suspend fun get(id: ID): T?
    suspend fun contains(id: ID): ID?
    suspend fun delete(id: ID)
    suspend fun delete(ids: List<ID>)
    suspend fun deleteAll()
    suspend fun update(entity: T)
    suspend fun list(): List<T>
    fun flowList(): Flow<List<T>>
    fun dataSourceFactory(): DataSource.Factory<Int, T>
}

