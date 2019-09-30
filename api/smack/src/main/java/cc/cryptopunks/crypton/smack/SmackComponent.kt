package cc.cryptopunks.crypton.smack

import cc.cryptopunks.crypton.api.ApiQualifier
import cc.cryptopunks.crypton.api.ApiScope
import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.entity.*
import cc.cryptopunks.crypton.smack.chat.ChatMessagePublisher
import cc.cryptopunks.crypton.smack.chat.SendChatMessage
import cc.cryptopunks.crypton.smack.presence.SendPresence
import cc.cryptopunks.crypton.smack.roster.RosterEventPublisher
import cc.cryptopunks.crypton.smack.user.AddContactUser
import cc.cryptopunks.crypton.smack.user.UserGetContacts
import cc.cryptopunks.crypton.smack.user.UserInvite
import cc.cryptopunks.crypton.smack.user.UserInvited
import dagger.Binds
import dagger.Component
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import org.jivesoftware.smack.AbstractXMPPConnection
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.chat2.ChatManager
import org.jivesoftware.smack.roster.Roster
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration
import org.jivesoftware.smackx.iqregister.AccountManager
import org.jivesoftware.smackx.mam.MamManager
import org.jivesoftware.smackx.muc.MultiUserChatManager
import org.jivesoftware.smackx.offline.OfflineMessageManager
import org.jivesoftware.smackx.omemo.OmemoManager

@ApiScope
@Component(modules = [SmackComponent.Module::class])
internal interface SmackComponent : Client {

    @get:ApiQualifier
    override val accountId: Long

    @get:ApiQualifier
    override val address: Address

    @dagger.Module(includes = [Bindings::class])
    class Module(
        @get:Provides @get:ApiQualifier val accountId: Long,
        @get:Provides @get:ApiQualifier val address: Address,
        @get:Provides val configuration: XMPPTCPConnectionConfiguration
    ) {

        @Provides
        @ApiScope
        fun user(connection: XMPPConnection) = User(
            address = connection.user.remoteId()
        )

        @Provides
        @ApiScope
        fun tcpConnection() = XMPPTCPConnection(configuration).apply {
            connect()
        }

        @Provides
        @ApiScope
        fun accountManager(connection: XMPPConnection) =
            AccountManager.getInstance(connection)!!.apply {
                sensitiveOperationOverInsecureConnection(true)
            }

        @Provides
        @ApiScope
        fun mamManager(connection: XMPPConnection) = MamManager.getInstanceFor(connection)!!

        @Provides
        @ApiScope
        fun chatManager(connection: XMPPConnection) = ChatManager.getInstanceFor(connection)!!

        @Provides
        @ApiScope
        fun multiUserChatManager(connection: XMPPConnection) =
            MultiUserChatManager.getInstanceFor(connection)!!

        @Provides
        @ApiScope
        fun roster(connection: XMPPConnection) = Roster.getInstanceFor(connection)!!.apply {
            subscriptionMode = Roster.SubscriptionMode.accept_all
        }

        @Provides
        @ApiScope
        fun offlineMessageManager(connection: XMPPConnection) = OfflineMessageManager(connection)

        @Provides
        @ApiScope
        fun omemoManager(connection: XMPPConnection) = OmemoManager.getInstanceFor(connection)!!

        @Provides
        @ApiScope
        @ApiQualifier
        fun disposable() = CompositeDisposable()
    }

    @dagger.Module
    interface Bindings {

        @Binds
        fun abstractConnection(instance: XMPPTCPConnection): AbstractXMPPConnection

        @Binds
        fun connection(instance: AbstractXMPPConnection): XMPPConnection

        @Binds
        fun create(instance: CreateClient): Client.Create

        @Binds
        fun remove(instance: RemoveClient): Client.Remove

        @Binds
        fun connect(instance: ConnectClient): Client.Connect

        @Binds
        fun disconnect(instance: DisconnectClient): Client.Disconnect

        @Binds
        fun login(instance: LoginClient): Client.Login

        @Binds
        fun isAuthenticated(instance: IsClientAuthentificated): Client.IsAuthenticated

        @Binds
        fun sendMessage(instance: SendChatMessage): Message.Api.Send

        @Binds
        fun sendPresence(instance: SendPresence): Presence.Api.Send

        @Binds
        fun chatMessagePublisher(instance: ChatMessagePublisher): Message.Api.Publisher

        @Binds
        fun getUserContacts(instance: UserGetContacts): User.Api.GetContacts

        @Binds
        fun inviteUser(instance: UserInvite): User.Api.Invite

        @Binds
        fun invitedUser(instance: UserInvited): User.Api.Invited

        @Binds
        fun rosterEventPublisher(instance: RosterEventPublisher): RosterEvent.Api.Publisher

        @Binds
        fun addContact(addContact: AddContactUser): User.Api.AddContact
    }
}