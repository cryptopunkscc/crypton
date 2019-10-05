package cc.cryptopunks.crypton.smack

import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.entity.*
import cc.cryptopunks.crypton.smack.chat.ChatMessagePublisher
import cc.cryptopunks.crypton.smack.chat.SendChatMessage
import cc.cryptopunks.crypton.smack.presence.SendPresence
import cc.cryptopunks.crypton.smack.roster.RosterEventPublisher
import cc.cryptopunks.crypton.smack.roster.RosterRxAdapter
import cc.cryptopunks.crypton.smack.user.AddContactUser
import cc.cryptopunks.crypton.smack.user.UserGetContacts
import cc.cryptopunks.crypton.smack.user.UserInvite
import cc.cryptopunks.crypton.smack.user.UserInvited
import io.reactivex.disposables.CompositeDisposable
import org.jivesoftware.smack.chat2.ChatManager
import org.jivesoftware.smack.roster.Roster
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration
import org.jivesoftware.smackx.iqregister.AccountManager

class SmackClient(
    override val accountId: Long,
    override val address: Address,
    configuration: XMPPTCPConnectionConfiguration
) : Client {

    private val disposable = CompositeDisposable()

    private val connection by lazy {
        XMPPTCPConnection(configuration).apply {
            connect()
        }
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
    private val rxAdapter by lazy {
        RosterRxAdapter()
    }
    override val create: Client.Create by lazy {
        CreateClient(
            configuration = configuration,
            accountManager = accountManager
        )
    }
    override val remove: Client.Remove by lazy {
        RemoveClient(accountManager = accountManager)
    }

    override val login: Client.Login by lazy {
        LoginClient(connection = connection)
    }

    override val connect: Client.Connect by lazy {
        ConnectClient(connection = connection)
    }

    override val disconnect: Client.Disconnect by lazy {
        DisconnectClient(connection = connection)
    }

    override val isAuthenticated: Client.IsAuthenticated by lazy {
        IsClientAuthenticated(connection = connection)
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
        SendChatMessage(connection = connection)
    }

    override val messagePublisher: Message.Api.Publisher by lazy {
        ChatMessagePublisher(
            chatManager = chatManager,
            disposable = disposable,
            address = address
        )
    }

    override val rosterEventPublisher: RosterEvent.Api.Publisher by lazy {
        RosterEventPublisher(
            disposable = CompositeDisposable(),
            roster = roster,
            adapter = rxAdapter
        )
    }
}