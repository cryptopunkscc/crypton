package cc.cryptopunks.crypton.util.reactivebindings

import android.view.View
import cc.cryptopunks.crypton.util.CacheFlow
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect

suspend fun View.bind(property: CacheFlow<Long>) = flowClicks().collect {
    property { plus(1) }
}

fun View.flowClicks(): Flow<Unit> = callbackFlow {
    setOnClickListener { channel.offer(Unit) }
    awaitClose { setOnClickListener(null) }
}