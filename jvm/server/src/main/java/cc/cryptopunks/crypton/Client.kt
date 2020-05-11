package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.Route
import cc.cryptopunks.crypton.util.typedLog
import io.ktor.network.selector.ActorSelectorManager
import io.ktor.network.sockets.aSocket
import io.ktor.network.sockets.openReadChannel
import io.ktor.network.sockets.openWriteChannel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.net.InetSocketAddress

private class Client

private suspend fun createSocketConnector(
    host: String = "127.0.0.1",
    port: Int = 2323
) = aSocket(ActorSelectorManager(Dispatchers.IO)).tcp()
    .connect(InetSocketAddress(host, port))
    .run {
        SocketConnector(
            log = Client().typedLog(),
            inputChannel = openReadChannel(),
            outputChannel = openWriteChannel(autoFlush = true)
        )
    }


fun startClient1() = runBlocking {

    createSocketConnector().apply {
        launch {
            inputChannel.flowParsedMessages().collect {
                log.d("Received $it")
            }
        }
        send(
            Route.Login,
            Account.Service.Set(Account.Field.ServiceName, "janek-latitude"),
            Account.Service.Set(Account.Field.UserName, "test1"),
            Account.Service.Set(Account.Field.Password, "test1"),
            Account.Service.Login,
            Route.Roster
        )
    }
}

fun startClient2() = runBlocking {

    createSocketConnector().apply {
        launch {
            inputChannel.flowParsedMessages().collect {
                log.d("Received $it")
            }
        }
        send(
            Route.Login,
            Account.Service.Set(Account.Field.ServiceName, "janek-latitude"),
            Account.Service.Set(Account.Field.UserName, "test2"),
            Account.Service.Set(Account.Field.Password, "test2"),
            Account.Service.Login,
            Route.Roster
        )
    }
}
