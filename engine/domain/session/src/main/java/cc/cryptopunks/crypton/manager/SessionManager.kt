package cc.cryptopunks.crypton.manager

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Session
import cc.cryptopunks.crypton.factory.SessionFactory
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    private val createSession: SessionFactory
) : Flow<Session.Event> {

    private var current: Address? = null
    private val events = BroadcastChannel<Session.Event>(Channel.CONFLATED)
    private val sessions = mutableMapOf<Address, Session>()

    operator fun get(account: Address): Session = synchronized(this) {
        var created = false
        sessions.getOrPut(account) {
            created = true
            createSession(account)
        }.apply {
            if (created)
                connectBroadcast()
        }
    }

    private fun Session.connectBroadcast() {
        scope.launch {
            events.send(Event(Session.Created))
            netEvents.collect { event ->
                events.send(Event(event))
            }
        }
    }

    operator fun contains(account: Address): Boolean = synchronized(this) {
        account in sessions
    }

    operator fun minus(account: Address): Unit = synchronized(this) {
        sessions.remove(account)?.run {
            scope.cancel()
        }
        if (current == account)
            current = getFirstConnectedAddress()
    }

    private fun getFirstConnectedAddress() = sessions.entries
        .firstOrNull { it.value.isAuthenticated() }?.key

    fun getCurrent(): Session? = sessions.values.firstOrNull() //sessions[current]

    fun isCurrent(session: Session): Boolean = session.address == current

    fun eventsFlow() = events.asFlow()

    @InternalCoroutinesApi
    override suspend fun collect(collector: FlowCollector<Session.Event>) =
        events.asFlow().collect(collector)

    interface Core {
        val sessionManager: SessionManager
    }
}