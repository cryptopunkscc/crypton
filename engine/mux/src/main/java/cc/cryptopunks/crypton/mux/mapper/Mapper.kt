package cc.cryptopunks.crypton.mux.mapper

import cc.cryptopunks.crypton.util.toByteArray
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import java.nio.ByteBuffer

fun ByteArray.withSize() = size.toLong().toByteArray() + this

fun Flow<ByteArray>.withSize(size: Long) = flow {
    emit(size.toByteArray())
    emitAll(this@withSize)
}


suspend fun ReceiveChannel<ByteArray>.readBytes() = flow<ByteArray> {
    val buffer = ByteBuffer.wrap(receive())
    val size = buffer.long
    var remaining = size




}
