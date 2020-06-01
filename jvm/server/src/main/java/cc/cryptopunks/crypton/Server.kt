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
import io.ktor.network.sockets.Socket
import io.ktor.network.sockets.aSocket
import io.ktor.network.sockets.openReadChannel
import io.ktor.network.sockets.openWriteChannel
import io.ktor.util.cio.write
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
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
                ChatBackgroundServiceModule(sessionCore).chatBackgroundService,
                RosterBackgroundServiceModule(sessionCore).rosterBackgroundService,
                SessionDomainModule(sessionCore).sessionService
            )
        }
    )
}

suspend fun startServer() = coroutineScope {
    initSmack(File("./omemo_store"))

    val backendService = BackendService(core)

    val server = aSocket(ActorSelectorManager(Dispatchers.IO)).tcp()
        .bind(InetSocketAddress("127.0.0.1", 2323))

    log.d("Started at ${server.localAddress}")

    while (true) {
        val socket = server.accept()
        log.d("Socket accepted: ${socket.remoteAddress}")
//        launch {
//            delay(3000)
//            println("sending dupa")
//            socket.openWriteChannel().run {
//                write("dupa")
//                flush()
//            }
//        }
//        launch(Dispatchers.IO) {
//            log.d("Receiving")
//            socket.connector(log).input.collect {
//                log.d("Received $it")
//            }
//        }
        backendService.handle(socket)
    }
}

private fun BackendService.handle(socket: Socket) = launch {
    try {
        socket.connector(log).apply {
            log.d("Initialing")
//            launch { init() }
            log.d("Initialized")
            connect()
        }
    } catch (e: Throwable) {
        e.printStackTrace()
        socket.close()
    }
}
