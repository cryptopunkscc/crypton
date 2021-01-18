package cc.cryptopunks.crypton.agent

import cc.cryptopunks.crypton.Action
import cc.cryptopunks.crypton.fsv2.ByteFlowStore
import cc.cryptopunks.crypton.plus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.withIndex

internal class ByteStreamChunk(
    val id: String,
    val index: Int,
    val bytes: ByteArray,
) : Action

internal fun ByteFlowStore.fileStreamChunks(ids: Set<String>): Flow<ByteStreamChunk> =
    ids.asFlow().flatMapMerge { fileStreamChunks(it) }


internal fun ByteFlowStore.fileStreamChunks(id: String): Flow<ByteStreamChunk> = get(id).withIndex()
    .map { (index, bytes) -> ByteStreamChunk(id, index, bytes) }
    .plus(flowOf(ByteStreamChunk(id, -1, EmptyByteArray)))

private val EmptyByteArray = ByteArray(0)
