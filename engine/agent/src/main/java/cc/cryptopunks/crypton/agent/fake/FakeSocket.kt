package cc.cryptopunks.crypton.agent.fake

import cc.cryptopunks.crypton.agent.OpenConnection
import cc.cryptopunks.crypton.agent.Socket
import cc.cryptopunks.crypton.agent.SocketConnections
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.consumeAsFlow

fun fakeEndpoint(id: String) = "fake:$id"

val openFakeConnection: OpenConnection = { address ->
    val server = requireNotNull(servers[address])

    val left = Channel<ByteArray>(Channel.BUFFERED)
    val right = Channel<ByteArray>(Channel.BUFFERED)

    val leftSocket = Socket(
        input = left.consumeAsFlow(),
        output = right::send
    )

    val rightSocket = Socket(
        input = right.consumeAsFlow(),
        output = left::send
    )

    server.send(rightSocket)

    delay(100)

    leftSocket
}

val fakeSocketConnections: SocketConnections = { address ->
    require(address !in servers).let {
        Channel<Socket>(Channel.BUFFERED).also {
            servers[address] = it
        }.consumeAsFlow()
    }
}

private val servers = mutableMapOf<String, SendChannel<Socket>>()
