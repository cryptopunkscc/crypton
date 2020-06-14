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
import cc.cryptopunks.crypton.smack.net.chat.OutgoingMessageCache
import cc.cryptopunks.crypton.smack.net.chat.ReadArchivedMessages
import cc.cryptopunks.crypton.smack.net.chat.createChat
import cc.cryptopunks.crypton.smack.net.chat.createMessageEventBroadcast
import cc.cryptopunks.crypton.smack.net.chat.createSendMessage
import cc.cryptopunks.crypton.smack.net.client.InitOmemo
import cc.cryptopunks.crypton.smack.net.roster.rosterEventFlow
import cc.cryptopunks.crypton.smack.util.SmackPresence
import cc.cryptopunks.crypton.smack.util.bareJid
import cc.cryptopunks.crypton.smack.util.presence
import cc.cryptopunks.crypton.smack.util.remoteId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import org.jxmpp.jid.impl.JidCreate
import org.jxmpp.jid.parts.Localpart

internal class ConnectionModule(
    scope: CoroutineScope,
    private val address: Address,
    private val smack: SmackCore
) : SmackCore by smack,
    Connection {

    private val initOmemo by lazy { InitOmemo(omemoManager) }

    private val outgoingMessageCache by lazy {
        OutgoingMessageCache()
    }

    private val netEvents by lazy {
        NetEventBroadcast(
            scope = scope,
            connection = connection,
            initOmemo = initOmemo
        )
    }

    private val messageEventBroadcast by lazy {
        createMessageEventBroadcast(
            scope = scope,
            chatManager = chatManager,
            omemoManager = omemoManager
        )
    }

    private val sendMessage by lazy {
        createSendMessage(
            address = address,
            connection = connection,
            roster = roster,
            omemoManager = omemoManager,
            broadcast = messageEventBroadcast
        )
    }

    private val readArchived by lazy {
        ReadArchivedMessages(
            connection = connection,
            omemoManager = omemoManager,
            multiUserChatManager = mucManager
        )
    }


    override fun netEvents(): Flow<Api.Event> = netEvents.flow()

    override fun isConnected(): Boolean = connection.isConnected

    override fun initOmemo(): Boolean = initOmemo.invoke()

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

    override fun getContacts(): List<User> = roster.entries.map { entry ->
        User(address = entry.jid.remoteId())
    }

    override fun addContact(user: User) {
        roster.createEntry(
            user.address.bareJid(),
            user.address.local,
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
            )
        )
    }

    override suspend fun sendMessage(message: Message) = sendMessage.invoke(message)

    override fun messageEvents(): Flow<Message.Net.Event> = messageEventBroadcast.asFlow()

    override fun readArchived(
        query: Message.Net.ReadArchived.Query
    ): Flow<List<Message>> = readArchived.invoke(query)

    override val rosterEvents: Flow<Roster.Net.Event> get() = roster.rosterEventFlow()

    override fun createChat(chat: Chat): Chat = smack.createChat(chat)

    override fun getCachedPresences(): List<Presence> = roster.run {
        entries.map { entry -> getPresence(entry.jid).presence(entry.jid) }
    }
}

private fun <T> SmackCore.lazy(init: SmackCore.() -> T) = kotlin.lazy { init() }
