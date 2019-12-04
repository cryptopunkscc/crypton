package cc.cryptopunks.crypton.util.ext

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

inline fun <T, R> Flow<T>.map(crossinline transform: (value: T) -> R): Flow<R> = map { transform(it) }
