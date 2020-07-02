package cc.cryptopunks.crypton.smack

import kotlinx.coroutines.CoroutineScope
import org.jivesoftware.smack.chat2.ChatManager
import org.jivesoftware.smack.roster.Roster
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration
import org.jivesoftware.smackx.carbons.CarbonManager
import org.jivesoftware.smackx.iqregister.AccountManager
import org.jivesoftware.smackx.mam.MamManager
import org.jivesoftware.smackx.muc.MultiUserChatManager
import org.jivesoftware.smackx.omemo.OmemoManager

internal interface SmackCore : CoroutineScope {
    val configuration: XMPPTCPConnectionConfiguration
    val connection: XMPPTCPConnection
    val accountManager: AccountManager
    val roster: Roster
    val chatManager: ChatManager
    val mucManager: MultiUserChatManager
    val mamManager: MamManager
    val omemoManager: OmemoManager
    val carbonManager: CarbonManager
}
