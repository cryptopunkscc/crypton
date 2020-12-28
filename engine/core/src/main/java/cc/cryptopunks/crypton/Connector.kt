package cc.cryptopunks.crypton

import kotlinx.coroutines.flow.Flow

data class Connector(
    val input: Flow<Any>,
    val output: Output = {},
) {
    suspend fun Any.out() = output()
}
