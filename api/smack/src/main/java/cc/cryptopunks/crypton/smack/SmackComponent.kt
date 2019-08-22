package cc.cryptopunks.crypton.smack

import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.api.ApiQualifier
import cc.cryptopunks.crypton.api.ApiScope
import cc.cryptopunks.crypton.api.entities.ChatMessage
import cc.cryptopunks.crypton.api.entities.Presence
import cc.cryptopunks.crypton.api.entities.RosterEvent
import cc.cryptopunks.crypton.api.entities.User
import cc.cryptopunks.crypton.smack.chat.ChatMessagePublisher
import cc.cryptopunks.crypton.smack.chat.SendChatMessage
import cc.cryptopunks.crypton.smack.presence.SendPresence
import cc.cryptopunks.crypton.smack.roster.RosterEventPublisher
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
    override val id: Long

    @dagger.Module(includes = [Bindings::class])
    class Module(
        @get:Provides @get:ApiQualifier val accountId: Long,
        @get:Provides val configuration: XMPPTCPConnectionConfiguration
    ) {

        @Provides
        @ApiScope
        fun user(connection: XMPPConnection) = User(
            jid = connection.user.jid()
        )

        @Provides
        @ApiScope
        fun tcpConnection() = XMPPTCPConnection(configuration).apply {
            connect()
        }

        @Provides
        @ApiScope
        fun accountManager(connection: XMPPConnection) = AccountManager.getInstance(connection)!!.apply {
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
        fun multiUserChatManager(connection: XMPPConnection) = MultiUserChatManager.getInstanceFor(connection)!!

        @Provides
        @ApiScope
        fun roster(connection: XMPPConnection) = Roster.getInstanceFor(connection)!!.apply {
            subscriptionMode = Roster.SubscriptionMode.reject_all
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
        @ApiScope
        fun abstractConnection(instance: XMPPTCPConnection): AbstractXMPPConnection

        @Binds
        @ApiScope
        fun connection(instance: AbstractXMPPConnection): XMPPConnection

        @Binds
        @ApiScope
        fun register(instance: CreateClient): Client.Create

        @Binds
        @ApiScope
        fun unregister(instance: RemoveClient): Client.Remove

        @Binds
        @ApiScope
        fun connect(instance: ConnectClient): Client.Connect

        @Binds
        @ApiScope
        fun disconnect(instance: DisconnectClient): Client.Disconnect

        @Binds
        @ApiScope
        fun login(instance: LoginClient): Client.Login

        @Binds
        @ApiScope
        fun isAuthenticated(instance: IsClientAuthentificated): Client.IsAuthenticated

        @Binds
        @ApiScope
        fun sendMessage(instance: SendChatMessage): ChatMessage.Send

        @Binds
        @ApiScope
        fun sendPresence(instance: SendPresence): Presence.Send

        @Binds
        @ApiScope
        fun chatMessagePublisher(instance: ChatMessagePublisher): ChatMessage.Publisher

        @Binds
        @ApiScope
        fun getUserContacts(instance: UserGetContacts): User.GetContacts

        @Binds
        @ApiScope
        fun inviteUser(instance: UserInvite): User.Invite

        @Binds
        @ApiScope
        fun invitedUser(instance: UserInvited): User.Invited

        @Binds
        @ApiScope
        fun rosterEventPublisher(instance: RosterEventPublisher): RosterEvent.Publisher
    }
}