package cc.cryptopunks.crypton.api

import cc.cryptopunks.crypton.common.RxPublisher
import cc.cryptopunks.crypton.common.createDummyClass
import cc.cryptopunks.kache.rxjava.flowable
import cc.cryptopunks.crypton.api.entities.*
import dagger.Provides
import io.reactivex.processors.PublishProcessor
import org.reactivestreams.Subscriber
import javax.inject.Inject
import javax.inject.Singleton

interface Client {

    val id: Long
    val user: User
    val create: Create
    val remove: Remove
    val login: Login
    val connect: Connect
    val disconnect: Disconnect
    val sendMessage: ChatMessage.Send
    val sendPresence: Presence.Send
    val isAuthenticated: IsAuthenticated
    val getContacts: User.GetContacts
    val invite: User.Invite
    val invited: User.Invited

    val chatMessagePublisher: ChatMessage.Publisher
    val rosterEventPublisher: RosterEvent.Publisher

    interface Create: () -> Unit
    interface Remove: () -> Unit
    interface Login: () -> Unit
    interface Connect: () -> Unit
    interface Disconnect: () -> Unit
    interface IsAuthenticated: () -> Boolean

    interface Factory : (Config) -> Client {
        data class Arg(
            val accountId: Int = EmptyId,
            val jid: Jid = Jid(),
            val password: String = ""
        ) {
            companion object {
                const val EmptyId = -1
                val Empty = Arg()
            }
        }
    }

    data class Config(
        val id: Long = EmptyId,
        val jid: Jid = Jid(),
        val password: String = ""
    ) {
        companion object {
            const val EmptyId = 0L
            val Empty = Config()
        }
    }

    @Singleton
    class Publisher @Inject constructor(cache: Cache) : RxPublisher<Client> by cache

    @Singleton
    class Cache(
        private val map: MutableMap<Long, Client>,
        private val processor: PublishProcessor<Client> = PublishProcessor.create()
    ) :
        MutableMap<Long, Client> by map,
        RxPublisher<Client> {

        @Inject
        constructor() : this(mutableMapOf())


        override fun subscribe(subscriber: Subscriber<in Client>) {
            map.values.forEach(subscriber::onNext)
            processor.subscribe(subscriber)
        }

        override fun remove(key: Long) = map
            .remove(key)
            ?.apply {
                processor.onNext(
                    Empty(
                        id = id
                    )
                )
            }
        override fun put(key: Long, value: Client): Client? = map
            .put(key, value)
            .apply {
                processor.onNext(value)
            }
    }

    class Empty(override val id: Long) : Client by DummyClient

    @dagger.Module
    class Module(@get:Provides val client: Client) : Client {
        override val user: User @Provides get() = client.user
        override val id: Long @Provides get() = client.id
        override val create: Create @Provides get() = client.create
        override val remove: Remove @Provides get() = client.remove
        override val connect: Connect @Provides get() = client.connect
        override val disconnect: Disconnect @Provides get() = client.disconnect
        override val login: Login @Provides get() = client.login
        override val sendMessage: ChatMessage.Send @Provides get() = client.sendMessage
        override val sendPresence: Presence.Send @Provides get() = client.sendPresence
        override val isAuthenticated: IsAuthenticated @Provides get() = client.isAuthenticated
        override val getContacts: User.GetContacts @Provides get() = client.getContacts
        override val invite: User.Invite @Provides get() = client.invite
        override val invited: User.Invited @Provides get() = client.invited

        override val chatMessagePublisher: ChatMessage.Publisher @Provides get() = client.chatMessagePublisher
        override val rosterEventPublisher: RosterEvent.Publisher @Provides get() = client.rosterEventPublisher
    }

    companion object {
        private val DummyClient: Client = createDummyClass()
    }
}

val Client.isEmpty get() = this is Client.Empty

fun Client.Publisher.filter(user: User) = flowable().filter { it.user == user }!!