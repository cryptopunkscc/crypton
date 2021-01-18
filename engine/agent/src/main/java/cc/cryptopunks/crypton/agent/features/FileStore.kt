package cc.cryptopunks.crypton.agent.features

import cc.cryptopunks.crypton.Action
import cc.cryptopunks.crypton.Async
import cc.cryptopunks.crypton.agent.ByteStreamChunk
import cc.cryptopunks.crypton.agent.fileStreamChunks
import cc.cryptopunks.crypton.create.build
import cc.cryptopunks.crypton.create.cryptonContext
import cc.cryptopunks.crypton.create.emitter
import cc.cryptopunks.crypton.create.handler
import cc.cryptopunks.crypton.delegate.dep
import cc.cryptopunks.crypton.fsv2.ByteFlowStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

const val FILE_STORE = "FileStore"

val CoroutineScope.fileStore: ByteFlowStore by dep(FILE_STORE)

object StoreRequest {
    class Add(val bytes: ByteArray) : Action
    data class Remove(val id: String) : Action
    data class Get(val id: String) : Async
}

data class FileAdded(val id: String) : Action
data class FileRemoved(val id: String) : Action

fun fileStore() = cryptonContext(

    emitter { fileStore.additions().map { FileAdded(it) } },
    emitter { fileStore.deletions().map { FileRemoved(it) } },
    handler { _, action: StoreRequest.Add -> fileStore + flowOf(action.bytes) },
    handler { _, (id): StoreRequest.Remove -> fileStore - id },
    handler { out, (id): StoreRequest.Get -> fileStore.fileStreamChunks(id).collect(out) },
    build {
        val write = byteStreamWriter(fileStore)
        handler { _, chunk: ByteStreamChunk -> write(chunk) }
    }
)

private fun byteStreamWriter(
    fileStore: ByteFlowStore,
    channels: MutableMap<String, Channel<ByteArray>> = mutableMapOf(),
): suspend (ByteStreamChunk) -> Unit = { stream ->
    when (stream.index) {
        0 -> {
            channels
                .getOrPut(stream.id) { Channel(Channel.BUFFERED) }
                .apply { offer(stream.bytes) }
                .consumeAsFlow()
        }
        -1 -> null.also {
            channels.remove(stream.id)?.close()
        }
        else -> null.also {
            channels.getValue(stream.id).send(stream.bytes)
        }
    }?.let { bytesFlow ->
        coroutineScope {
            launch { fileStore.plus(bytesFlow) }
        }
    }
}
