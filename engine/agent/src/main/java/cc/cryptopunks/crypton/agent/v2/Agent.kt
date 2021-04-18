package cc.cryptopunks.crypton.agent.v2

import cc.cryptopunks.crypton.agent.Broadcast
import cc.cryptopunks.crypton.agent.Socket
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

typealias HandleConnection = suspend (Socket) -> Unit
typealias ConnectSocket = suspend (String) -> Socket
typealias SocketConnections = (String) -> Flow<Socket>
typealias EncodeAddress = (String) -> ByteArray
typealias DecodeAddress = (ByteArray) -> String

data class Agent(
    val address: String,
    val broadcast: Broadcast,
    val handle: HandleConnection,
    val connect: ConnectSocket,
    val connections: SocketConnections,
    val decodeAddress: DecodeAddress,
    val encodeAddress: EncodeAddress,
)

suspend fun Agent.invoke(): Unit = coroutineScope {
    joinAll(
        launch { broadcast.input.collect { handle(connect(decodeAddress(it))) } },
        launch { broadcast.output(encodeAddress(address)) },
        launch { connections(address).collect(handle) }
    )
}

