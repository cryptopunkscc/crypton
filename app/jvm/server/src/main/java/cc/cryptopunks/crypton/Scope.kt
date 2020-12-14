package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.context.Connection
import cc.cryptopunks.crypton.context.RootScope
import cc.cryptopunks.crypton.context.context
import cc.cryptopunks.crypton.context.createRootScope
import cc.cryptopunks.crypton.repo.ormlite.OrmLiteAppRepo
import cc.cryptopunks.crypton.service.cryptonFeatures
import cc.cryptopunks.crypton.service.cryptonResolvers
import cc.cryptopunks.crypton.smack.SmackConnectionFactory
import cc.cryptopunks.crypton.smack.initSmack
import cc.cryptopunks.crypton.sys.JvmSys
import cc.cryptopunks.crypton.util.IOExecutor
import cc.cryptopunks.crypton.util.MainExecutor
import cc.cryptopunks.crypton.util.ormlite.jvm.createJdbcH2ConnectionSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import java.io.File


fun createServerScope(config: Map<String, Any?>): RootScope = RootScopeConfig(config).run {
    initSmack(File(omemoStorePath))
    val features = cryptonFeatures()
    createRootScope(
        cryptonContext(
            JvmSys(home).context(),
            OrmLiteAppRepo { name: String ->
                createJdbcH2ConnectionSource(
                    home = "$home/$profile/",
                    name = name,
                    inMemory = inMemory.toBoolean()
                )
            }.context(),
            IOExecutor(Dispatchers.IO.asExecutor()),
            MainExecutor(Dispatchers.IO.asExecutor()),
            createConnectionFactory(this).asDep(),
            features,
            features.createHandlers(),
            cryptonResolvers()
        )
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
