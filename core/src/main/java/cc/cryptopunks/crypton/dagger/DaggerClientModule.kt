package cc.cryptopunks.crypton.dagger

import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.entity.*
import dagger.Module
import dagger.Provides

@Module
class DaggerClientModule(@get:Provides val client: Client) :
    Client {
    override val address: Address @Provides get() = client.address
    override val accountId: Long @Provides get() = client.accountId
    override val create: Client.Create @Provides get() = client.create
    override val remove: Client.Remove @Provides get() = client.remove
    override val connect: Client.Connect @Provides get() = client.connect
    override val disconnect: Client.Disconnect @Provides get() = client.disconnect
    override val login: Client.Login @Provides get() = client.login
    override val sendMessage: Message.Api.Send @Provides get() = client.sendMessage
    override val sendPresence: Presence.Api.Send @Provides get() = client.sendPresence
    override val isAuthenticated: Client.IsAuthenticated @Provides get() = client.isAuthenticated
    override val getContacts: User.Api.GetContacts @Provides get() = client.getContacts
    override val addContact: User.Api.AddContact @Provides get() = client.addContact
    override val invite: User.Api.Invite @Provides get() = client.invite
    override val invited: User.Api.Invited @Provides get() = client.invited
    override val messagePublisher: Message.Api.Publisher @Provides get() = client.messagePublisher
    override val rosterEventPublisher: RosterEvent.Api.Publisher @Provides get() = client.rosterEventPublisher
}