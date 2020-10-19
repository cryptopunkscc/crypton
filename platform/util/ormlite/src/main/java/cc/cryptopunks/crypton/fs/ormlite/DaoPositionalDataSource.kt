package cc.cryptopunks.crypton.fs.ormlite

import androidx.paging.DataSource
import androidx.paging.PositionalDataSource
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.stmt.Where

class DaoPositionalDataSource<T : Any, ID>(
    private val dao: Dao<T, ID>,
    private val orderColumn: String = "timestamp",
    private val where: Where<T, ID>.() -> Where<T, ID> = { this },
) : PositionalDataSource<T>() {

    private val observer = Dao.DaoObserver { invalidate() }
    private var callbacksCount: Int = 0

    override fun loadInitial(
        params: LoadInitialParams,
        callback: LoadInitialCallback<T>,
    ) {
        callback.onResult(
            dao.queryBuilder()
                .orderBy(orderColumn, false)
                .offset(params.requestedStartPosition.toLong())
                .limit(params.requestedLoadSize.toLong())
                .where()
                .where()
                .query(),
            params.requestedStartPosition,
            dao.count()
        )
    }

    override fun loadRange(
        params: LoadRangeParams,
        callback: LoadRangeCallback<T>,
    ) {

        callback.onResult(
            dao.queryBuilder()
                .orderBy(orderColumn, false)
                .offset(params.startPosition.toLong())
                .limit(params.loadSize.toLong())
                .where()
                .where()
                .query()
        )
    }

    override fun addInvalidatedCallback(onInvalidatedCallback: InvalidatedCallback) {
        super.addInvalidatedCallback(onInvalidatedCallback)
        if (callbacksCount++ == 0) dao.registerObserver(observer)
    }

    override fun removeInvalidatedCallback(onInvalidatedCallback: InvalidatedCallback) {
        if (--callbacksCount == 0) dao.unregisterObserver(observer)
        super.removeInvalidatedCallback(onInvalidatedCallback)
    }

    class Factory<T : Any, ID>(
        private val dao: Dao<T, ID>,
        private val orderColumn: String = "timestamp",
        private val where: Where<T, ID>.() -> Where<T, ID> = { this },
        ) : DataSource.Factory<Int, T>() {
        override fun create(): DataSource<Int, T> = DaoPositionalDataSource(
            dao = dao,
            orderColumn = orderColumn,
            where = where
        )
    }
}
