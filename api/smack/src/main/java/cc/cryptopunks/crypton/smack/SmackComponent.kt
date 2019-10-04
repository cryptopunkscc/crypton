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
        IsClientAuthentificated(connection = connection)
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
//
//@ApiScope
//@Component(modules = [SmackComponent.Module::class])
//internal interface SmackComponent : Client {
//
//    @get:ApiQualifier
//    override val accountId: Long
//
//    @get:ApiQualifier
//    override val address: Address
//
//    @dagger.Module(includes = [Bindings::class])
//    class Module(
//        @get:Provides @get:ApiQualifier val accountId: Long,
//        @get:Provides @get:ApiQualifier val address: Address,
//        @get:Provides val configuration: XMPPTCPConnectionConfiguration
//    ) {
//
//        @Provides
//        @ApiScope
//        fun user(connection: XMPPConnection) = User(
//            address = connection.user.remoteId()
//        )
//
//        @Provides
//        @ApiScope
//        fun tcpConnection() = XMPPTCPConnection(configuration).apply {
//            connect()
//        }
//
//        @Provides
//        @ApiScope
//        fun accountManager(connection: XMPPConnection) =
//            AccountManager.getInstance(connection)!!.apply {
//                sensitiveOperationOverInsecureConnection(true)
//            }
//
//        @Provides
//        @ApiScope
//        fun mamManager(connection: XMPPConnection) = MamManager.getInstanceFor(connection)!!
//
//        @Provides
//        @ApiScope
//        fun chatManager(connection: XMPPConnection) = ChatManager.getInstanceFor(connection)!!
//
//        @Provides
//        @ApiScope
//        fun multiUserChatManager(connection: XMPPConnection) =
//            MultiUserChatManager.getInstanceFor(connection)!!
//
//        @Provides
//        @ApiScope
//        fun roster(connection: XMPPConnection) = Roster.getInstanceFor(connection)!!.apply {
//            subscriptionMode = Roster.SubscriptionMode.accept_all
//        }
//
//        @Provides
//        @ApiScope
//        fun offlineMessageManager(connection: XMPPConnection) = OfflineMessageManager(connection)
//
//        @Provides
//        @ApiScope
//        fun omemoManager(connection: XMPPConnection) = OmemoManager.getInstanceFor(connection)!!
//
//        @Provides
//        @ApiScope
//        @ApiQualifier
//        fun disposable() = CompositeDisposable()
//    }
//
//    @dagger.Module
//    interface Bindings {
//
//        @Binds
//        fun abstractConnection(instance: XMPPTCPConnection): AbstractXMPPConnection
//
//        @Binds
//        fun connection(instance: AbstractXMPPConnection): XMPPConnection
//
//        @Binds
//        fun create(instance: CreateClient): Client.Create
//
//        @Binds
//        fun remove(instance: RemoveClient): Client.Remove
//
//        @Binds
//        fun connect(instance: ConnectClient): Client.Connect
//
//        @Binds
//        fun disconnect(instance: DisconnectClient): Client.Disconnect
//
//        @Binds
//        fun login(instance: LoginClient): Client.Login
//
//        @Binds
//        fun isAuthenticated(instance: IsClientAuthentificated): Client.IsAuthenticated
//
//        @Binds
//        fun sendMessage(instance: SendChatMessage): Message.Api.Send
//
//        @Binds
//        fun sendPresence(instance: SendPresence): Presence.Api.Send
//
//        @Binds
//        fun chatMessagePublisher(instance: ChatMessagePublisher): Message.Api.Publisher
//
//        @Binds
//        fun getUserContacts(instance: UserGetContacts): User.Api.GetContacts
//
//        @Binds
//        fun inviteUser(instance: UserInvite): User.Api.Invite
//
//        @Binds
//        fun invitedUser(instance: UserInvited): User.Api.Invited
//
//        @Binds
//        fun rosterEventPublisher(instance: RosterEventPublisher): RosterEvent.Api.Publisher
//
//        @Binds
//        fun addContact(addContact: AddContactUser): User.Api.AddContact
//    }
//}