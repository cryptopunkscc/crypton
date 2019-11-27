package cc.cryptopunks.crypton.manager

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Session
import cc.cryptopunks.crypton.context.Session.Event.Created
import cc.cryptopunks.crypton.factory.SessionFactory
import cc.cryptopunks.crypton.util.Broadcast
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collect
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    private val createSession: SessionFactory
) : Flow<Session.Event> {

    private val broadcast = Broadcast<Session.Event>()
    private val sessions = mutableMapOf<Address, Session>()
    private var current: Address? = null

    private val firstConnectedAddress
        get() = sessions.entries.firstOrNull { it.value.isAuthenticated() }?.key

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
            broadcast.send(sessionEvent(Created))
            netEvents.collect { event ->
                broadcast.send(sessionEvent(event))
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
            current = firstConnectedAddress
    }

    fun getCurrent(): Session? = sessions.values.firstOrNull() //sessions[current]

    fun isCurrent(session: Session): Boolean =
        session.address == current

    @InternalCoroutinesApi
    override suspend fun collect(collector: FlowCollector<Session.Event>) =
        broadcast.collect(collector)

    interface Core {
        val sessionManager: SessionManager
    }
}