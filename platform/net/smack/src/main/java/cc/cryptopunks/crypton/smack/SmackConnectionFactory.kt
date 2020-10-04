package cc.cryptopunks.crypton.smack

import cc.cryptopunks.crypton.context.Connection
import cc.cryptopunks.crypton.smack.module.createConnection
import org.jivesoftware.smack.ConnectionConfiguration
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration
import org.jivesoftware.smackx.omemo.OmemoConfiguration
import org.jivesoftware.smackx.omemo.signal.SignalCachingOmemoStore
import org.jivesoftware.smackx.omemo.signal.SignalFileBasedOmemoStore
import org.jivesoftware.smackx.omemo.signal.SignalOmemoService
import java.io.File
import java.net.InetAddress

private var initialized = false

fun initSmack(omemoStoreFile: File) {
    if (initialized) return
    OmemoConfiguration.setRepairBrokenSessionsWithPrekeyMessages(true)
    SignalOmemoService.acknowledgeLicense()
    SignalOmemoService.setup()
    SignalOmemoService.getInstance().apply {
        omemoStoreBackend = SignalCachingOmemoStore(SignalFileBasedOmemoStore(omemoStoreFile))
    }
    initialized = true
}

class SmackConnectionFactory(
    setup: Connection.Factory.Config.() -> Unit = { }
) : Connection.Factory {

    private val factoryConfig = Connection.Factory.Config().apply(setup)

    override fun invoke(config: Connection.Config): Connection = createConnection(
        scope = config.scope,
        account = config.account,
        configuration = connectionConfig
            .apply {
                if (config.resource.isNotEmpty())
                    setResource(config.resource)
            }
            .setUsernameAndPassword(
                config.account.local,
                String(config.password.byteArray)
            )
            .setXmppDomain(config.account.domain)
            .build()
    )

    private val connectionConfig
        get() = XMPPTCPConnectionConfiguration.builder()
            .enableDefaultDebugger()
//            .setResource(factoryConfig.resource)
            .setHostAddress(factoryConfig.hostAddress?.let(InetAddress::getByName))
            .setSecurityMode(ConnectionConfiguration.SecurityMode.valueOf(factoryConfig.securityMode.name))
}
