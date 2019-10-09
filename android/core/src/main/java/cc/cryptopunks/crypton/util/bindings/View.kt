package cc.cryptopunks.crypton.util.bindings

import android.view.View
import cc.cryptopunks.crypton.util.CacheFlow
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect

suspend fun View.bind(property: CacheFlow<Long>) = clicks().collect {
    property { plus(1) }
}

fun View.clicks(): Flow<Unit> = callbackFlow {
    setOnClickListener { channel.offer(Unit) }
    awaitClose { setOnClickListener(null) }
}