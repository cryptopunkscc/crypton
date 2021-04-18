package cc.cryptopunks.crypton.fsv2

import cc.cryptopunks.crypton.fs.util.sha256MessageDigest
import cc.cryptopunks.crypton.fs.util.toHex
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileFilter
import java.io.InputStream
import java.io.OutputStream
import java.util.*

class FileStore(
    private val dir: File,
) : ByteFlowStore {

    init {
        dir.mkdir()
    }

    private val additions = BroadcastChannel<Id>(Channel.BUFFERED)
    private val deletions = BroadcastChannel<Id>(Channel.BUFFERED)

    override fun get(id: Id): Flow<ByteArray> = dir.resolve(id).inputStream().asFlow()

    override fun contains(id: Id): Boolean = dir.resolve(id).run { exists() && isFile }

    override fun list(): Set<Id> = dir.listFiles(FileFilter { it.isDirectory })
        ?.map { it.name }?.toSet()
        ?: emptySet()

    override fun additions(): Flow<Id> = additions.asFlow()

    override fun deletions(): Flow<Id> = deletions.asFlow()

    override suspend fun plus(data: Flow<ByteArray>): Id =
        dir.resolve(UUID.randomUUID().toString()).run {
            outputStream().write(data).toHex().also { id ->
                renameTo(dir.resolve(id))
                additions.offer(id)
            }
        }

    override fun minus(id: Id): Boolean =
        dir.resolve(id).delete().also {
            if (it) deletions.offer(id)
        }

}

//fun InputStream.asFlow(bufferSize: Int = 506) = flow {
private fun InputStream.asFlow(bufferSize: Int = 8) = flow {
    var len = 0
    while (len != -1) {
        ByteArray(bufferSize).also {
            len = read(it)
        }.run {
            when {
                len == bufferSize -> this
                len > 0 -> copyOf(len)
                else -> null
            }
        }?.let {
            emit(it)
        }
    }
}

suspend fun OutputStream.write(bytes: Flow<ByteArray>): ByteArray = sha256MessageDigest().apply {
    use {
        bytes
            .flowOn(Dispatchers.IO)
            .onEach { println("CHUNK ========== ${it.toString(Charsets.UTF_8)}") }
            .onEach { update(it) }
            .collect { write(it) }
    }
}.digest()

