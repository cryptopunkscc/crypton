package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.context.Connection
import cc.cryptopunks.crypton.context.RootModule
import cc.cryptopunks.crypton.context.RootScope
import cc.cryptopunks.crypton.mock.MockSys
import cc.cryptopunks.crypton.repo.ormlite.OrmLiteAppRepo
import cc.cryptopunks.crypton.service.cryptonHandlers
import cc.cryptopunks.crypton.smack.SmackConnectionFactory
import cc.cryptopunks.crypton.smack.initSmack
import cc.cryptopunks.crypton.util.IOExecutor
import cc.cryptopunks.crypton.util.MainExecutor
import cc.cryptopunks.crypton.util.ormlite.jvm.createJdbcH2ConnectionSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import java.io.File

fun Server.init() {
    initSmack(File(config.omemoStorePath))
}

fun Server.Config.rootScope(): RootScope =
    RootModule(
        sys = MockSys(),
        repo = OrmLiteAppRepo { name: String ->
            createJdbcH2ConnectionSource(
                home = "$home/",
                name = name,
                inMemory = inMemory.toBoolean()
            )
        },
        mainClass = Nothing::class,
        ioExecutor = IOExecutor(Dispatchers.IO.asExecutor()),
        mainExecutor = MainExecutor(Dispatchers.IO.asExecutor()),
        createConnection = createConnectionFactory(this),
        handlers = cryptonHandlers()
    )


private fun createConnectionFactory(config: Server.Config) =
    SmackConnectionFactory {
        hostAddress = config.hostAddress
        securityMode = Connection.Factory.Config.SecurityMode.valueOf(config.securityMode)
    } as Connection.Factory
