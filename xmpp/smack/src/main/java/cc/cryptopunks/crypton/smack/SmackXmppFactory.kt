package cc.cryptopunks.crypton.smack

import cc.cryptopunks.crypton.xmpp.Xmpp
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration


object SmackXmppFactory : Xmpp.Factory, (Xmpp.Config) -> Xmpp {


    private var configBuilder = XMPPTCPConnectionConfiguration.builder()

    private val setup: XMPPTCPConnectionConfiguration.Builder.() -> XMPPTCPConnectionConfiguration.Builder = { this }

    operator fun invoke(setup: XMPPTCPConnectionConfiguration.Builder.() -> XMPPTCPConnectionConfiguration.Builder) = apply {
        configBuilder = configBuilder.setup()
    }

    override fun invoke(config: Xmpp.Config): Xmpp = cc.cryptopunks.crypton.smack.DaggerSmackComponent.builder().module(
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