package cc.cryptopunks.crypton.mux

import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel

typealias GetChannel = (Short) -> SendChannel<ByteArray>
typealias CreateChannel = () -> Pair<Short, ReceiveChannel<ByteArray>>

