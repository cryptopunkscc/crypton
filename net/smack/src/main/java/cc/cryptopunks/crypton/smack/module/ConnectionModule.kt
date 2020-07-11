package cc.cryptopunks.crypton.smack.module

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Api
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Connection
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Presence
import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.context.User
import cc.cryptopunks.crypton.smack.SmackCore
import cc.cryptopunks.crypton.smack.net.api.NetEventBroadcast
import cc.cryptopunks.crypton.smack.net.chat.ChatNet
import cc.cryptopunks.crypton.smack.net.chat.createMuc
import cc.cryptopunks.crypton.smack.net.chat.invitationsFlow
import cc.cryptopunks.crypton.smack.net.message.MessageNet
import cc.cryptopunks.crypton.smack.net.omemo.InitOmemo
import cc.cryptopunks.crypton.smack.net.roster.rosterEventFlow
import cc.cryptopunks.crypton.smack.util.SmackPresence
import cc.cryptopunks.crypton.smack.util.address
import cc.cryptopunks.crypton.smack.util.entityBareJid
import cc.cryptopunks.crypton.smack.util.presence
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration
import org.jxmpp.jid.impl.JidCreate
import org.jxmpp.jid.parts.Localpart


internal fun createConnection(
    scope: CoroutineScope,
    address: Address,
    configuration: XMPPTCPConnectionConfiguration
) = ConnectionModule(
    scope = scope,
    address = address,
    smack = SmackModule(
        configuration = configuration,
        address = address,
        scope = scope
    )
)

internal class ConnectionModule(
    scope: CoroutineScope,
    private val address: Address,
    private val smack: SmackCore
) : SmackCore by smack,
    Connection,
    Message.Net by MessageNet(smack),
    Chat.Net by ChatNet(smack) {

    private val log = typedLog()
    private val initOmemo by lazy { InitOmemo(omemoManager) }

    private val netEvents by lazy {
        NetEventBroadcast(
            scope = scope,
            connection = connection,
            initOmemo = initOmemo
        )
    }

    override fun netEvents(): Flow<Api.Event> = netEvents.flow()

    override fun isConnected(): Boolean = connection.isConnected

    override suspend fun initOmemo() = initOmemo.invoke()

    override fun isOmemoInitialized(): Boolean = initOmemo.isInitialized

    override fun connect() {
        connection.run {
            if (!isConnected)
                connect()
        }
    }

    override fun disconnect() {
        connection.disconnect()
    }


    override fun interrupt() {
        connection.instantShutdown()
    }

    override fun createAccount() {
        accountManager.createAccount(
            Localpart.from(configuration.username.toString()),
            configuration.password
        )
    }

    override fun removeAccount() {
        accountManager.deleteAccount()
    }

    override fun login() {
        connection.login()
        carbonManager.enableCarbons()
    }

    override fun isAuthenticated() =
        connection.isAuthenticated

    override fun getContacts(): List<Address> = roster.entries.map { entry ->
        entry.jid.address()
    }

    override fun addContact(user: Address) {
        roster.createEntry(
            user.entityBareJid(),
            user.local,
            null
        )
    }

    override fun invite(address: Address) {
        connection.sendStanza(
            SmackPresence(
                JidCreate.from(address.toString()),
                org.jivesoftware.smack.packet.Presence.Type.subscribe
            )
        )
    }

    override fun invited(address: Address) {
        connection.sendStanza(
            SmackPresence(
                JidCreate.from(address.toString()),
                org.jivesoftware.smack.packet.Presence.Type.subscribed
            )
        )
    }

    override fun sendPresence(presence: Presence) {
        connection.sendStanza(
            SmackPresence(
                org.jivesoftware.smack.packet.Presence.Type.fromString(
                    presence.status.name.toLowerCase()
                )
            ).apply {
                when (presence.status) {
                    Presence.Status.Subscribed,
                    Presence.Status.Subscribe -> to = presence.resource.address.entityBareJid()
                }
            }.also {
                log.d("Sending $presence")
            }
        )
    }

    override fun iAmSubscribed(address: Address) = roster.iAmSubscribedTo(address.entityBareJid())

    override fun subscribe(address: Address) = roster.createEntry(address.entityBareJid(), address.local, emptyArray())


    override val rosterEvents: Flow<Roster.Net.Event> get() = roster.rosterEventFlow()

    override fun createConference(chat: Chat): Chat = smack.createMuc(chat)

    override fun mucInvitationsFlow() = mucManager.invitationsFlow()


    override fun getCachedPresences(): List<Presence> = roster.run {
        entries.map { entry -> getPresence(entry.jid).presence(entry.jid) }
    }
}

private fun <T> SmackCore.lazy(init: SmackCore.() -> T) = kotlin.lazy { init() }
