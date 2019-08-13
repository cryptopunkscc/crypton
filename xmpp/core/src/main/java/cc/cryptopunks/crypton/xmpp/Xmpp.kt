package cc.cryptopunks.crypton.xmpp

import cc.cryptopunks.crypton.common.RxPublisher
import cc.cryptopunks.crypton.common.createDummyClass
import cc.cryptopunks.kache.rxjava.flowable
import cc.cryptopunks.crypton.xmpp.entities.*
import dagger.Provides
import io.reactivex.processors.PublishProcessor
import org.reactivestreams.Subscriber
import javax.inject.Inject
import javax.inject.Singleton

interface Xmpp {

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

    interface Factory : (Config) -> Xmpp {
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
    class Publisher @Inject constructor(cache: Cache) : RxPublisher<Xmpp> by cache

    @Singleton
    class Cache(
        private val map: MutableMap<Long, Xmpp>,
        private val processor: PublishProcessor<Xmpp> = PublishProcessor.create()
    ) :
        MutableMap<Long, Xmpp> by map,
        RxPublisher<Xmpp> {

        @Inject
        constructor() : this(mutableMapOf())


        override fun subscribe(subscriber: Subscriber<in Xmpp>) {
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
        override fun put(key: Long, value: Xmpp): Xmpp? = map
            .put(key, value)
            .apply {
                processor.onNext(value)
            }
    }

    class Empty(override val id: Long) : Xmpp by DummyXmpp

    @dagger.Module
    class Module(@get:Provides val xmpp: Xmpp) :
        Xmpp {
        override val user: User @Provides get() = xmpp.user
        override val id: Long @Provides get() = xmpp.id
        override val create: Create @Provides get() = xmpp.create
        override val remove: Remove @Provides get() = xmpp.remove
        override val connect: Connect @Provides get() = xmpp.connect
        override val disconnect: Disconnect @Provides get() = xmpp.disconnect
        override val login: Login @Provides get() = xmpp.login
        override val sendMessage: ChatMessage.Send @Provides get() = xmpp.sendMessage
        override val sendPresence: Presence.Send @Provides get() = xmpp.sendPresence
        override val isAuthenticated: IsAuthenticated @Provides get() = xmpp.isAuthenticated
        override val getContacts: User.GetContacts @Provides get() = xmpp.getContacts
        override val invite: User.Invite @Provides get() = xmpp.invite
        override val invited: User.Invited @Provides get() = xmpp.invited

        override val chatMessagePublisher: ChatMessage.Publisher @Provides get() = xmpp.chatMessagePublisher
        override val rosterEventPublisher: RosterEvent.Publisher @Provides get() = xmpp.rosterEventPublisher
    }

    companion object {
        private val DummyXmpp: Xmpp = createDummyClass()
    }
}

val Xmpp.isEmpty get() = this is Xmpp.Empty

fun Xmpp.Publisher.filter(user: User) = flowable().filter { it.user == user }!!