package cc.cryptopunks.crypton.smack.module

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.logv2.basicLog
import cc.cryptopunks.crypton.smack.SmackCore
import kotlinx.coroutines.CoroutineScope
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.chat2.ChatManager
import org.jivesoftware.smack.roster.Roster
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration
import org.jivesoftware.smackx.carbons.CarbonManager
import org.jivesoftware.smackx.httpfileupload.HttpFileUploadManager
import org.jivesoftware.smackx.iqregister.AccountManager
import org.jivesoftware.smackx.mam.MamManager
import org.jivesoftware.smackx.muc.MultiUserChatManager
import org.jivesoftware.smackx.omemo.OmemoManager

internal class SmackModule(
    override val configuration: XMPPTCPConnectionConfiguration,
    val account: Address,
    scope: CoroutineScope
) : SmackCore,
    CoroutineScope by scope {

    override val log = basicLog

    // Smack
    override val connection by lazy {
        XMPPTCPConnection(configuration).apply {
            replyTimeout = 10000 // Omemo initialization can take longer then 5s
            fromMode = XMPPConnection.FromMode.USER
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

    override val mamManager: MamManager by lazy {
        MamManager.getInstanceFor(connection)
    }

    override val omemoManager: OmemoManager by lazy {
        OmemoManager.getInstanceFor(connection)
    }

    override val carbonManager: CarbonManager by lazy {
        CarbonManager.getInstanceFor(connection)!!
    }

    override val httpFileUploadManager by lazy {
        HttpFileUploadManager.getInstanceFor(connection)!!
    }
}
