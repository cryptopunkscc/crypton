package cc.cryptopunks.crypton.smack

import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.entity.*
import cc.cryptopunks.crypton.smack.account.*
import cc.cryptopunks.crypton.smack.chat.MessageBroadcast
import cc.cryptopunks.crypton.smack.chat.SendMessage
import cc.cryptopunks.crypton.smack.client.ConnectClient
import cc.cryptopunks.crypton.smack.client.DisconnectClient
import cc.cryptopunks.crypton.smack.presence.SendPresence
import cc.cryptopunks.crypton.smack.roster.RosterEventPublisher
import cc.cryptopunks.crypton.smack.user.AddContactUser
import cc.cryptopunks.crypton.smack.user.UserGetContacts
import cc.cryptopunks.crypton.smack.user.UserInvite
import cc.cryptopunks.crypton.smack.user.UserInvited
import org.jivesoftware.smack.chat2.ChatManager
import org.jivesoftware.smack.roster.Roster
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration
import org.jivesoftware.smackx.iqregister.AccountManager

class SmackClient(
    override val address: Address,
    configuration: XMPPTCPConnectionConfiguration
) : Client {

    private val connection by lazy {
        XMPPTCPConnection(configuration)
    }

    private val accountManager by lazy {
        AccountManager.getInstance(connection).apply {
            sensitiveOperationOverInsecureConnection(true)
        }
    }

    private val roster by lazy {
        Roster.getInstanceFor(connection).apply {
            subscriptionMode = Roster.SubscriptionMode.accept_all
        }
    }

    private val chatManager by lazy {
        ChatManager.getInstanceFor(connection)
    }

    override val connect: Client.Connect by lazy {
        ConnectClient(connection = connection)
    }

    override val disconnect: Client.Disconnect by lazy {
        DisconnectClient(connection = connection)
    }

    override val isConnected: Client.IsConnected by lazy {
        IsAccountConnected(connection = connection)
    }

    override val createAccount: Account.Api.Create by lazy {
        CreateAccount(
            configuration = configuration,
            accountManager = accountManager
        )
    }
    override val remove: Account.Api.Remove by lazy {
        RemoveAccount(accountManager = accountManager)
    }

    override val login: Account.Api.Login by lazy {
        LoginAccount(connection = connection)
    }

    override val isAuthenticated: Account.Api.IsAuthenticated by lazy {
        IsAccountAuthenticated(connection = connection)
    }

    override val getContacts: User.Api.GetContacts by lazy {
        UserGetContacts(roster = roster)
    }

    override val addContact: User.Api.AddContact by lazy {
        AddContactUser(roster = roster)
    }

    override val invite: User.Api.Invite by lazy {
        UserInvite(connection = connection)
    }

    override val invited: User.Api.Invited by lazy {
        UserInvited(connection = connection)
    }

    override val sendPresence: Presence.Api.Send by lazy {
        SendPresence(connection = connection)
    }

    override val sendMessage: Message.Api.Send by lazy {
        SendMessage(connection = connection)
    }

    override val messageBroadcast: Message.Api.Broadcast by lazy {
        MessageBroadcast(
            chatManager = chatManager,
            address = address
        )
    }

    override val rosterEventPublisher: RosterEvent.Api.Broadcast by lazy {
        RosterEventPublisher(roster = roster)
    }
}