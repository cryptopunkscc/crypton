package cc.cryptopunks.crypton

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

fun systemInput() = flow {
    do {
        readLine()?.let {
            emit(it)
        } ?: delay(100)
    } while (true)
}
