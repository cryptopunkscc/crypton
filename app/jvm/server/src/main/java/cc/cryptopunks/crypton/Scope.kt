package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.context.Connection
import cc.cryptopunks.crypton.context.RootModule
import cc.cryptopunks.crypton.repo.ormlite.OrmLiteAppRepo
import cc.cryptopunks.crypton.service.cryptonFeatures
import cc.cryptopunks.crypton.smack.SmackConnectionFactory
import cc.cryptopunks.crypton.smack.initSmack
import cc.cryptopunks.crypton.sys.JvmSys
import cc.cryptopunks.crypton.util.IOExecutor
import cc.cryptopunks.crypton.util.MainExecutor
import cc.cryptopunks.crypton.util.ormlite.jvm.createJdbcH2ConnectionSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import java.io.File


fun createRootScope(config: Map<String, Any?>) = RootScopeConfig(config).run {
    initSmack(File(omemoStorePath))
    RootModule(
        sys = JvmSys(home),
        repo = OrmLiteAppRepo { name: String ->
            createJdbcH2ConnectionSource(
                home = "$home/$profile/",
                name = name,
                inMemory = inMemory.toBoolean()
            )
        },
        mainClass = Nothing::class,
        ioExecutor = IOExecutor(Dispatchers.IO.asExecutor()),
        mainExecutor = MainExecutor(Dispatchers.IO.asExecutor()),
        createConnection = createConnectionFactory(this),
        features = cryptonFeatures()
    )
}

private class RootScopeConfig(
    map: Map<String, Any?>
) : MutableMap<String, Any?> by map.toMutableMap() {
    var home: String by this
    var profile: String by this
    var omemoStore: String by this
    var inMemory: String by this
    var hostAddress: String? by this
    var securityMode: String by this
}

private val RootScopeConfig.omemoStorePath get() = "$home/$profile/$omemoStore"

private fun createConnectionFactory(config: RootScopeConfig): Connection.Factory =
    SmackConnectionFactory {
        hostAddress = config.hostAddress
        securityMode = Connection.Factory.Config.SecurityMode.valueOf(config.securityMode)
    }
