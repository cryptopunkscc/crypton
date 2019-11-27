package cc.cryptopunks.crypton.smack

import cc.cryptopunks.crypton.context.Connection
import org.jivesoftware.smack.ConnectionConfiguration
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration
import org.jivesoftware.smackx.omemo.OmemoConfiguration
import org.jivesoftware.smackx.omemo.signal.SignalCachingOmemoStore
import org.jivesoftware.smackx.omemo.signal.SignalFileBasedOmemoStore
import org.jivesoftware.smackx.omemo.signal.SignalOmemoService
import java.io.File
import java.net.InetAddress

fun initSmack(omemoStoreFile: File) {
    OmemoConfiguration.setRepairBrokenSessionsWithPrekeyMessages(true)
    SignalOmemoService.acknowledgeLicense()
    SignalOmemoService.setup()
    SignalOmemoService.getInstance().apply {
        omemoStoreBackend = SignalCachingOmemoStore(SignalFileBasedOmemoStore(omemoStoreFile))
    }
}

class SmackConnectionFactory(
    setup: Connection.Factory.Config.() -> Connection.Factory.Config = { this }
) : Connection.Factory {

    private var factoryConfig = Connection.Factory.Config.Empty

    init {
        invoke(setup)
    }

    operator fun invoke(setup: Connection.Factory.Config.() -> Connection.Factory.Config) = apply {
        factoryConfig = factoryConfig.setup()
    }

    override fun invoke(config: Connection.Config): Connection = SmackConnection(
        scope = config.scope,
        address = config.address,
        configuration = connectionConfig
            .setUsernameAndPassword(
                config.address.local,
                config.password.toString()
            )
            .setXmppDomain(config.address.domain)
            .build()
    )

    private val connectionConfig
        get() = XMPPTCPConnectionConfiguration.builder()
            .enableDefaultDebugger()
//            .setResource(factoryConfig.resource)
            .setHostAddress(factoryConfig.hostAddress?.let(InetAddress::getByName))
            .setSecurityMode(ConnectionConfiguration.SecurityMode.valueOf(factoryConfig.securityMode.name))
}