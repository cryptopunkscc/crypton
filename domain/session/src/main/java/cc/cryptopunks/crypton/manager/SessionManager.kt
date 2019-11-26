package cc.cryptopunks.crypton.manager

import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.entity.Address
import cc.cryptopunks.crypton.entity.Session
import cc.cryptopunks.crypton.entity.Session.Event.Created
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

    operator fun get(account: Account): Session = synchronized(this) {
        var created = false
        sessions.getOrPut(account.address) {
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

    operator fun contains(account: Account): Boolean = synchronized(this) {
        account.address in sessions
    }

    operator fun minus(account: Account): Unit = synchronized(this) {
        val address = account.address
        sessions.remove(address)?.run {
            scope.cancel()
        }
        if (current == address)
            current = firstConnectedAddress
    }

    fun getCurrent(): Session? = sessions.values.firstOrNull() //sessions[current]

    fun isCurrent(session: Session): Boolean =
        session.address == current

    @InternalCoroutinesApi
    override suspend fun collect(collector: FlowCollector<Session.Event>) =
        broadcast.collect(collector)

    interface Component {
        val sessionManager: SessionManager
    }
}