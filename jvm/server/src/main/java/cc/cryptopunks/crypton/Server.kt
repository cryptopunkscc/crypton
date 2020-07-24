package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.backend.BackendService
import cc.cryptopunks.crypton.backend.internal.mainHandlers
import cc.cryptopunks.crypton.context.AppModule
import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.context.Connection
import cc.cryptopunks.crypton.mock.MockRepo
import cc.cryptopunks.crypton.mock.MockSys
import cc.cryptopunks.crypton.net.connect
import cc.cryptopunks.crypton.net.startServerSocket
import cc.cryptopunks.crypton.service.appHandlers
import cc.cryptopunks.crypton.service.chatHandlers
import cc.cryptopunks.crypton.smack.SmackConnectionFactory
import cc.cryptopunks.crypton.smack.initSmack
import cc.cryptopunks.crypton.util.IOExecutor
import cc.cryptopunks.crypton.util.Log
import cc.cryptopunks.crypton.util.MainExecutor
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.runBlocking
import java.io.File
import java.net.InetSocketAddress

fun main() {
    Log.init(JvmLog)
    TrustAllManager.install()
    runBlocking { startCryptonServer() }
}

suspend fun startCryptonServer() {
    initSmack(File("./omemo_store"))
    startServerSocket(address, log).connect(
        log = log,
        connectable = BackendService(appScope)
    )
}

private val address = InetSocketAddress("127.0.0.1", 2323)

private val log = Server.typedLog()

private object Server

private val appScope: AppScope
    get() = AppModule(
        sys = MockSys(),
        repo = MockRepo(),
        mainClass = Nothing::class,
        ioExecutor = IOExecutor(Dispatchers.IO.asExecutor()),
        mainExecutor = MainExecutor(Dispatchers.IO.asExecutor()),
        createConnection = createConnectionFactory,
        handlers = appHandlers + mainHandlers + chatHandlers
    )

private val createConnectionFactory = SmackConnectionFactory {
    hostAddress = "127.0.0.1"
    securityMode = Connection.Factory.Config.SecurityMode.disabled
}
