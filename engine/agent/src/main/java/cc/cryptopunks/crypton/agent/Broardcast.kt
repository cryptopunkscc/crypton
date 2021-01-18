package cc.cryptopunks.crypton.agent

import cc.cryptopunks.crypton.TypedConnector
import cc.cryptopunks.crypton.delegate.dep
import kotlinx.coroutines.CoroutineScope

const val BROADCAST = "BROADCAST"

val CoroutineScope.broadcast: Broadcast by dep(BROADCAST)

typealias Broadcast = TypedConnector<ByteArray>
