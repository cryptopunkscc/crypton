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
            accountId = config.id,
            configuration = configBuilder
                .run(setup)
                .setUsernameAndPassword(config.jid.login, config.password)
                .setXmppDomain(config.jid.domain)
                .build()
        )
    ).build()
}