package cc.cryptopunks.crypton.smack.net.chat

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Message.Net.Send
import org.jivesoftware.smack.packet.Message
import org.jivesoftware.smack.roster.Roster
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smackx.omemo.OmemoManager
import org.jxmpp.jid.impl.JidCreate

internal class SendMessage(
    connection: XMPPTCPConnection,
    omemoManager: OmemoManager,
    roster: Roster,
    encryptedMessageCache: EncryptedMessageCache
) : Send, (Address, String) -> Unit by { to, text ->

    val jid = JidCreate.entityBareFrom(to)

    if (!roster.iAmSubscribedTo(jid)) {
        roster.createEntry(jid, to.local, emptyArray())
    }

    val message = omemoManager
        .encrypt(jid, text)
        .asMessage(jid)
        .apply { type = Message.Type.chat }
    encryptedMessageCache[message.stanzaId] = text
//    val message = Message(jid, text)
    connection.sendStanza(message)
}