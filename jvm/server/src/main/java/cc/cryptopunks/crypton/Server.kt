package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.backend.BackendService
import cc.cryptopunks.crypton.backend.internal.mainHandlers
import cc.cryptopunks.crypton.context.AppModule
import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.context.Connection
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.mock.MockRepo
import cc.cryptopunks.crypton.mock.MockSys
import cc.cryptopunks.crypton.service.chatHandlers
import cc.cryptopunks.crypton.service.startSessionService
import cc.cryptopunks.crypton.smack.SmackConnectionFactory
import cc.cryptopunks.crypton.smack.initSmack
import cc.cryptopunks.crypton.util.IOExecutor
import cc.cryptopunks.crypton.util.Log
import cc.cryptopunks.crypton.util.MainExecutor
import cc.cryptopunks.crypton.util.typedLog
import io.ktor.network.selector.ActorSelectorManager
import io.ktor.network.sockets.ServerSocket
import io.ktor.network.sockets.Socket
import io.ktor.network.sockets.aSocket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import java.io.File
import java.net.InetSocketAddress

fun main() {
    Log.init(JvmLog)
    TrustAllManager.install()
    runBlocking { startServer() }
}

suspend fun startServer() = coroutineScope {
    initSmack(File("./omemo_store"))
    val service = BackendService(appScope)
    val server = startServerSocket()
    flow {
        while (true) emit(server.accept())
    }.onCompletion { throwable ->
        log.d("close server $server $throwable")
        service.cancel()
        service.appScope.cancel()
        server.close()
    }.onEach { socket ->
        log.d("Socket accepted: ${socket.remoteAddress}")
    }.collect { socket ->
        service.tryConnectTo(socket)
    }
}

private object Server

private val log = Server.typedLog()

private val appScope: AppScope
    get() = AppModule(
        sys = MockSys(),
        repo = MockRepo(),
        mainClass = Nothing::class,
        ioExecutor = IOExecutor(Dispatchers.IO.asExecutor()),
        mainExecutor = MainExecutor(Dispatchers.IO.asExecutor()),
        createConnection = createConnectionFactory,
        startSessionService = SessionScope::startSessionService,
        mainHandlers = mainHandlers,
        chatHandlers = chatHandlers
    )

private val createConnectionFactory = SmackConnectionFactory {
    hostAddress = "127.0.0.1"
    securityMode = Connection.Factory.Config.SecurityMode.disabled
}

private fun startServerSocket(): ServerSocket =
    aSocket(ActorSelectorManager(newSingleThreadContext("Server")))
        .tcp()
        .bind(InetSocketAddress("127.0.0.1", 2323))
        .apply { log.d("Started at $localAddress") }

private fun BackendService.tryConnectTo(socket: Socket) = let {
    try {
        socket.connector(log).connect().apply {
            invokeOnCompletion { socket.close() }
        }
    } catch (e: Throwable) {
        e.printStackTrace()
        socket.close()
    }
}
