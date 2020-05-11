package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.context.AppCore
import cc.cryptopunks.crypton.context.AppModule
import cc.cryptopunks.crypton.context.Engine
import cc.cryptopunks.crypton.mock.net.MockConnectionFactory
import cc.cryptopunks.crypton.module.AppDomainModule
import cc.cryptopunks.crypton.module.ChatBackgroundServiceModule
import cc.cryptopunks.crypton.module.RosterBackgroundServiceModule
import cc.cryptopunks.crypton.module.SessionDomainModule
import cc.cryptopunks.crypton.util.IOExecutor
import cc.cryptopunks.crypton.util.Log
import cc.cryptopunks.crypton.util.MainExecutor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.runBlocking

fun main() = App()

object App : Engine {
    override val core: AppCore by lazy {
        AppModule(
            sys = JvmSys(),
            repo = MockRepo(),
            mainClass = this::class,
            ioExecutor = IOExecutor(Dispatchers.IO.asExecutor()),
            mainExecutor = MainExecutor(Dispatchers.IO.asExecutor()),
            createConnection = MockConnectionFactory(),
            createSessionServices = { sessionCore ->
                listOf(
                    ChatBackgroundServiceModule(sessionCore).chatBackgroundService,
                    RosterBackgroundServiceModule(sessionCore).rosterBackgroundService,
                    SessionDomainModule(sessionCore).sessionService
                )
            }
        )
    }

    operator fun invoke() = runBlocking {
        Log.init(JvmLog)
        AppDomainModule(core).appService().join()
    }
}

