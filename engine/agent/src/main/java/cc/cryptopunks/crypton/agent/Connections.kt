package cc.cryptopunks.crypton.agent

import cc.cryptopunks.crypton.TypedConnector
import cc.cryptopunks.crypton.delegate.dep
import kotlinx.coroutines.CoroutineScope

val CoroutineScope.connections: Connections by dep()

class Connections {

    private val connections = mutableMapOf<IdentityId, TypedConnector<ByteArray>>()

    operator fun set(id: IdentityId, connector: TypedConnector<ByteArray>) {
        connections[id] = connector
    }

    operator fun get(id: IdentityId): TypedConnector<ByteArray>? =
        connections[id]
}
