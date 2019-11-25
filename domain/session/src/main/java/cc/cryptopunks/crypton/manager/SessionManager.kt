package cc.cryptopunks.crypton.manager

import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.entity.Address
import cc.cryptopunks.crypton.entity.Session
import cc.cryptopunks.crypton.factory.SessionFactory
import cc.cryptopunks.crypton.service.Service
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
    private val createSession: SessionFactory,
    private val scope: Service.Scope
) : Flow<Session.Event> {

    private val broadcast = Broadcast<Session.Event>()
    private val sessions = mutableMapOf<Address, Session>()
    private var current: Address? = null

    private val firstConnectedAddress
        get() = sessions.entries.firstOrNull { it.value.isAuthenticated() }?.key

    operator fun get(account: Account): Session = synchronized(this) {
        sessions.getOrPut(account.address) {
            createSession(account).also { session ->
                scope.launch {
                    session.netEvents.collect { event ->
                        broadcast.send(
                            Session.Event(
                                session = session,
                                event = event
                            )
                        )
                    }
                }
            }
        }
    }

    operator fun contains(account: Account): Boolean =
        account.address in sessions

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