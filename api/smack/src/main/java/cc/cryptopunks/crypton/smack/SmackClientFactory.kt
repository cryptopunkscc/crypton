package cc.cryptopunks.crypton.smack

import cc.cryptopunks.crypton.api.Client
import org.jivesoftware.smack.ConnectionConfiguration
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration
import java.net.InetAddress


object SmackClientFactory : Client.Factory, (Client.Config) -> Client {

    private var factoryConfig = Client.Factory.Config.Empty

    private val connectionConfig
        get() = XMPPTCPConnectionConfiguration.builder()
            .setResource(factoryConfig.resource)
            .setHostAddress(InetAddress.getByName(factoryConfig.hostAddress))
            .setSecurityMode(ConnectionConfiguration.SecurityMode.valueOf(factoryConfig.securityMode.name))

    operator fun invoke(setup: Client.Factory.Config.() -> Client.Factory.Config) = apply {
        factoryConfig = factoryConfig.setup()
    }

    override fun invoke(config: Client.Config): Client = SmackClient(
        address = config.address,
        configuration = connectionConfig
            .setUsernameAndPassword(config.address.local, config.password)
            .setXmppDomain(config.address.domain)
            .build()
    )
}