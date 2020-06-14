package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.backend.BackendService
import cc.cryptopunks.crypton.context.AppCore
import cc.cryptopunks.crypton.context.AppModule
import cc.cryptopunks.crypton.context.Connection
import cc.cryptopunks.crypton.mock.net.MockConnectionFactory
import cc.cryptopunks.crypton.module.ChatBackgroundServiceModule
import cc.cryptopunks.crypton.module.RosterBackgroundServiceModule
import cc.cryptopunks.crypton.module.SessionDomainModule
import cc.cryptopunks.crypton.smack.SmackConnectionFactory
import cc.cryptopunks.crypton.smack.initSmack
import cc.cryptopunks.crypton.util.IOExecutor
import cc.cryptopunks.crypton.util.MainExecutor
import cc.cryptopunks.crypton.util.typedLog
import io.ktor.network.selector.ActorSelectorManager
import io.ktor.network.sockets.ServerSocket
import io.ktor.network.sockets.Socket
import io.ktor.network.sockets.aSocket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.io.File
import java.net.InetSocketAddress

private object Server

private val log = Server.typedLog()

private val createConnectionFactory = SmackConnectionFactory {
    copy(
        hostAddress = "127.0.0.1",
        securityMode = Connection.Factory.Config.SecurityMode.disabled
    )
}

private val core: AppCore by lazy {
    AppModule(
        sys = JvmSys(),
        repo = MockRepo(),
        mainClass = Nothing::class,
        ioExecutor = IOExecutor(Dispatchers.IO.asExecutor()),
        mainExecutor = MainExecutor(Dispatchers.IO.asExecutor()),
//        createConnection = MockConnectionFactory(),
        createConnection = createConnectionFactory,
        createSessionServices = { sessionCore ->
            listOf(
                SessionDomainModule(sessionCore).chatBackgroundService,
                RosterBackgroundServiceModule(sessionCore).rosterBackgroundService,
                SessionDomainModule(sessionCore).sessionService
            )
        }
    )
}

suspend fun startServer() = coroutineScope {
    initSmack(File("./omemo_store"))
    startServerSocket().run {
        val service = BackendService(core)
        while (true) {
            val socket = accept()
            log.d("Socket accepted: ${socket.remoteAddress}")
            service.tryConnectTo(socket)
        }
    }
}

private fun startServerSocket(): ServerSocket =
    aSocket(ActorSelectorManager(Dispatchers.IO))
        .tcp()
        .bind(InetSocketAddress("127.0.0.1", 2323))
        .apply { log.d("Started at $localAddress") }

private fun BackendService.tryConnectTo(socket: Socket) = launch {
    try {
        socket.connector(log).connect()
    } catch (e: Throwable) {
        e.printStackTrace()
        socket.close()
    }
}
