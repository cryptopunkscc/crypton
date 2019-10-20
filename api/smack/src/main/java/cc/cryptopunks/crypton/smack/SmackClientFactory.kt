package cc.cryptopunks.crypton.smack

import cc.cryptopunks.crypton.api.Api
import cc.cryptopunks.crypton.util.BroadcastError
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

class SmackClientFactory(
    private val broadcastError: BroadcastError = BroadcastError(),
    setup: Api.Factory.Config.() -> Api.Factory.Config = { this }
) : Api.Factory, (Api.Config) -> Api {

    private var factoryConfig = Api.Factory.Config.Empty

    init {
        invoke(setup)
    }

    operator fun invoke(setup: Api.Factory.Config.() -> Api.Factory.Config) = apply {
        factoryConfig = factoryConfig.setup()
    }

    override fun invoke(config: Api.Config): Api = SmackClient(
        address = config.address,
        configuration = connectionConfig
            .setUsernameAndPassword(config.address.local, config.password)
            .setXmppDomain(config.address.domain)
            .build(),
        broadcastError = broadcastError
    )

    private val connectionConfig
        get() = XMPPTCPConnectionConfiguration.builder()
            .enableDefaultDebugger()
//            .setResource(factoryConfig.resource)
            .setHostAddress(factoryConfig.hostAddress?.let(InetAddress::getByName))
            .setSecurityMode(ConnectionConfiguration.SecurityMode.valueOf(factoryConfig.securityMode.name))
}