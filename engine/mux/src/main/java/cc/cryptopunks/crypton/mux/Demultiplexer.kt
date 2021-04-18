package cc.cryptopunks.crypton.mux

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import java.nio.ByteBuffer


suspend fun Flow<ByteArray>.demux(
    channel: GetChannel,
) = collect { chunk ->
    val buffer = ByteBuffer.wrap(chunk)
    val id = buffer.short
    val data = ByteArray(buffer.remaining())
    buffer[data]
    channel(id).send(data)
}

