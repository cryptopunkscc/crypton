package androidx.paging

internal fun <T: Any> listDataSource(list: List<T>): DataSource<Int, T> = ListDataSource(list)
