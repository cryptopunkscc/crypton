package cc.cryptopunks.crypton.fs.ormlite

import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.DaoManager
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

inline fun <reified I, ID> ConnectionSource.createDao(): Dao<I, ID> =
    createDao(I::class.java)

fun <I, ID> ConnectionSource.createDao(type: Class<I>): Dao<I, ID> =
    DaoManager.createDao<Dao<I, ID>?, I>(this, type).also {
        TableUtils.createTableIfNotExists(this, type)
    }

fun <T> Dao<*, *>.changesFlow(block: suspend SendChannel<T>.() -> Unit): Flow<T> =
    callbackFlow {
        val scope = CoroutineScope(coroutineContext)
        Dao.DaoObserver {
            scope.launch { block() }
        }.let { observer ->
            registerObserver(observer)
            awaitClose { unregisterObserver(observer) }
        }
    }

