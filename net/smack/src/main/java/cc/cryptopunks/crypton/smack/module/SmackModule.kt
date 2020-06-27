package cc.cryptopunks.crypton.smack.module

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.smack.SmackCore
import cc.cryptopunks.crypton.smack.net.chat.MucInvitationManager
import cc.cryptopunks.crypton.smack.net.omemo.OmemoTrustAllCallback
import org.jivesoftware.smack.chat2.ChatManager
import org.jivesoftware.smack.roster.Roster
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration
import org.jivesoftware.smackx.carbons.CarbonManager
import org.jivesoftware.smackx.iqregister.AccountManager
import org.jivesoftware.smackx.mam.MamManager
import org.jivesoftware.smackx.muc.MultiUserChatManager
import org.jivesoftware.smackx.omemo.OmemoManager

internal class SmackModule(
    override val configuration: XMPPTCPConnectionConfiguration,
    val address: Address
) : SmackCore {

    // Smack
    override val connection by lazy {
        XMPPTCPConnection(configuration).apply {
            replyTimeout = 10000 // Omemo initialization can take longer then 5s
        }
    }

    override val accountManager by lazy {
        AccountManager.getInstance(connection)!!.apply {
            sensitiveOperationOverInsecureConnection(true)
        }
    }

    override val roster by lazy {
        Roster.getInstanceFor(connection)!!.apply {
            subscriptionMode = Roster.SubscriptionMode.accept_all
        }
    }

    override val chatManager by lazy {
        ChatManager.getInstanceFor(connection)!!
    }

    override val mucManager by lazy {
        MultiUserChatManager.getInstanceFor(connection)!!.apply {
            setAutoJoinOnReconnect(true)
        }
    }

    override val mucInvitationManager: MucInvitationManager by lazy {
        MucInvitationManager(address)
    }

    override val mamManager: MamManager by lazy {
        MamManager.getInstanceFor(connection)
    }

    override val omemoManager: OmemoManager by lazy {
        OmemoManager.getInstanceFor(connection)!!.apply {
            setTrustCallback(OmemoTrustAllCallback)
        }
    }

    override val carbonManager: CarbonManager by lazy {
        CarbonManager.getInstanceFor(connection)!!
    }

    // Crypton
}
