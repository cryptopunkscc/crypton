package cc.cryptopunks.crypton.fs.ormlite

import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.DaoManager
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils

inline fun <reified I, ID> ConnectionSource.createDao(): Dao<I, ID> =
    createDao(I::class.java)

fun <I, ID> ConnectionSource.createDao(type: Class<I>): Dao<I, ID> =
    DaoManager.createDao<Dao<I, ID>?, I>(this, type).also {
        TableUtils.createTableIfNotExists(this, type)
    }
