package cc.cryptopunks.crypton.smack

import cc.cryptopunks.crypton.api.Client
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration


object SmackClientFactory : Client.Factory, (Client.Config) -> Client {


    private var configBuilder = XMPPTCPConnectionConfiguration.builder()

    private val setup: XMPPTCPConnectionConfiguration.Builder.() -> XMPPTCPConnectionConfiguration.Builder = { this }

    operator fun invoke(setup: XMPPTCPConnectionConfiguration.Builder.() -> XMPPTCPConnectionConfiguration.Builder) = apply {
        configBuilder = configBuilder.setup()
    }

    override fun invoke(config: Client.Config): Client = DaggerSmackComponent.builder().module(
        SmackComponent.Module(
            accountId = config.accountId,
            address = config.address,
            configuration = configBuilder
                .run(setup)
                .setUsernameAndPassword(config.address.login, config.password)
                .setXmppDomain(config.address.domain)
                .build()
        )
    ).build()
}