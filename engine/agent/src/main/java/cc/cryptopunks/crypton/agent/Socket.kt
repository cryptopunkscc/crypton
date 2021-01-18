package cc.cryptopunks.crypton.agent

import cc.cryptopunks.crypton.TypedConnector
import cc.cryptopunks.crypton.delegate.dep
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

typealias Socket = TypedConnector<ByteArray>

typealias SocketConnections = (String) -> Flow<Socket>

typealias OpenConnection = suspend (String) -> Socket

const val OPEN_CONNECTION = "OpenConnection"

const val SOCKET_CONNECTIONS = "SocketConnections"

val CoroutineScope.socket: Socket by dep()

val CoroutineScope.openConnection: OpenConnection by dep(OPEN_CONNECTION)

val CoroutineScope.socketConnections: SocketConnections by dep(SOCKET_CONNECTIONS)
