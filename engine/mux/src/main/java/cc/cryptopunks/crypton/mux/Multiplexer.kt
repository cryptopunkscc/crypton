package cc.cryptopunks.crypton.mux

import cc.cryptopunks.crypton.util.toByteArray
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

const val BYTE_CHUNK_SIZE = 64
const val BYTE_CHUNK_DATA_SIZE = 62
const val BYTE_CHUNK_ID_SIZE = 2

fun SendChannel<ByteArray>.flowWriter(
    channelId: Short
): suspend (Flow<ByteArray>) -> Unit {
    val write = byteWriter(channelId)
    return { it.collect(write) }
}

fun SendChannel<ByteArray>.byteWriter(
    channelId: Short = 0,
): suspend (ByteArray) -> Unit {
    val id = channelId.toByteArray()
    return { data ->
        var from = 0
        var to = BYTE_CHUNK_DATA_SIZE

        while (to <= data.size) {
            send(id + data.copyOfRange(from, to))
            from = to
            to += BYTE_CHUNK_DATA_SIZE
        }

        if (from < data.size) {
            send(id + data.copyOfRange(from, data.size))
        }
    }
}
