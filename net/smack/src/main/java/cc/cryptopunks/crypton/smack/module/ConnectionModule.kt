package cc.cryptopunks.crypton.smack.module

import cc.cryptopunks.crypton.context.*
import cc.cryptopunks.crypton.smack.core.SmackCore
import cc.cryptopunks.crypton.smack.net.api.NetEventBroadcast
import cc.cryptopunks.crypton.smack.net.account.*
import cc.cryptopunks.crypton.smack.net.chat.*
import cc.cryptopunks.crypton.smack.net.client.ConnectNet
import cc.cryptopunks.crypton.smack.net.client.DisconnectNet
import cc.cryptopunks.crypton.smack.net.client.InitOmemo
import cc.cryptopunks.crypton.smack.net.client.InterruptNet
import cc.cryptopunks.crypton.smack.net.presence.GetCachedPresences
import cc.cryptopunks.crypton.smack.net.presence.SendPresence
import cc.cryptopunks.crypton.smack.net.roster.RosterEventBroadcast
import cc.cryptopunks.crypton.smack.net.user.AddContactUser
import cc.cryptopunks.crypton.smack.net.user.UserGetContacts
import cc.cryptopunks.crypton.smack.net.user.UserInvite
import cc.cryptopunks.crypton.smack.net.user.UserInvited
import cc.cryptopunks.crypton.util.BroadcastErrorScope

internal class ConnectionModule(
    scope: BroadcastErrorScope,
    private val address: Address,
    private val smack: SmackCore
) : SmackCore by smack,
    Connection {

    private val outgoingMessageCache by lazy {
        OutgoingMessageCache()
    }

    override val netEvents: Net.Output by lazy {
        NetEventBroadcast(
            scope = scope,
            connection = connection,
            initOmemo = initOmemo
        )
    }

    override val connect: Net.Connect by lazy {
        ConnectNet(connection = connection)
    }

    override val disconnect: Net.Disconnect by lazy {
        DisconnectNet(connection = connection)
    }

    override val interrupt: Net.Interrupt by lazy {
        InterruptNet(connection = connection)
    }

    override val isConnected: Net.IsConnected by lazy {
        IsAccountConnected(connection = connection)
    }

    override val initOmemo by lazy {
        InitOmemo(omemoManager)
    }

    override val createAccount: Account.Net.Create by lazy {
        CreateAccount(
            configuration = configuration,
            accountManager = accountManager
        )
    }
    override val removeAccount: Account.Net.Remove by lazy {
        RemoveAccount(accountManager = accountManager)
    }

    override val login: Account.Net.Login by lazy {
        LoginAccount(
            connection = connection,
            carbonManager = carbonManager
        )
    }

    override val isAuthenticated: Account.Net.IsAuthenticated by lazy {
        IsAccountAuthenticated(connection = connection)
    }

    override val getContacts: User.Net.GetContacts by lazy {
        UserGetContacts(roster = roster)
    }

    override val addContact: User.Net.AddContact by lazy {
        AddContactUser(roster = roster)
    }

    override val invite: User.Net.Invite by lazy {
        UserInvite(connection = connection)
    }

    override val invited: User.Net.Invited by lazy {
        UserInvited(connection = connection)
    }

    override val sendPresence: Presence.Net.Send by lazy {
        SendPresence(connection = connection)
    }

    override val sendMessage by lazy {
        SendMessage(
            address = address,
            connection = connection,
            roster = roster,
            omemoManager = omemoManager,
            outgoingMessageCache = outgoingMessageCache
        )
    }

    override val messageBroadcast: Message.Net.Broadcast by lazy {
        MessageBroadcast(
            scope = scope,
            sendMessage = sendMessage,
            chatManager = chatManager,
            address = address,
            omemoManager = omemoManager,
            outgoingMessageCache = outgoingMessageCache
        )
    }

    override val readArchived: Message.Net.ReadArchived by lazy {
        ReadArchivedMessages(
            connection = connection,
            omemoManager = omemoManager,
            multiUserChatManager = mucManager
        )
    }

    override val rosterEventPublisher: RosterEvent.Net.Broadcast by lazy {
        RosterEventBroadcast(roster = roster)
    }

    override val createChat: Chat.Net.Create by lazy(::CreateChat)

    override val getCached: UserPresence.Net.GetCached by lazy {
        GetCachedPresences(roster)
    }

    private val multiUserChatProvider by lazy {
        MultiUserChatProvider(
            account = address,
            muc = mucManager,
            invitationManager = mucInvitationManager,
            connection = connection
        )
    }

    override val multiUserChatFlow get() = multiUserChatProvider

    override val multiUserChatList get() = multiUserChatProvider
}

private fun <T> SmackCore.lazy(init: SmackCore.() -> T) = kotlin.lazy { init() }
