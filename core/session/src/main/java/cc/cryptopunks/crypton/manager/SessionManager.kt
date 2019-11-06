package cc.cryptopunks.crypton.manager

import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.entity.Address
import cc.cryptopunks.crypton.entity.Session
import cc.cryptopunks.crypton.factory.SessionFactory
import cc.cryptopunks.crypton.service.Service
import cc.cryptopunks.crypton.util.Broadcast
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collect
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    private val createSession: SessionFactory,
    private val scope: Service.Scope
) : Flow<Session.Status> {

    private val broadcast = Broadcast<Session.Status>()
    private val sessions = mutableMapOf<Address, Session>()
    private val statuses = mutableMapOf<Address, Account.Status>()
    private var current: Address? = null

    operator fun get(account: Account): Session = synchronized(this) {
        sessions.getOrPut(account.address) {
            createSession(account).also { session ->
                scope.launch {
                    session.statusFlow.collect { status ->
                        broadcast(
                            Session.Status(
                                session = session,
                                status = status
                            )
                        )
                    }
                }
            }
        }
    }

    operator fun get(session: Session): Account.Status =
        statuses[session.address] ?: Account.Status.Unknown

    operator fun contains(account: Account) =
        account.address in sessions

    operator fun minus(account: Account) = synchronized(this) {
        val address = account.address
        sessions -= address
        statuses -= address
        if (current == address)
            current = statuses.entries.maxBy { it.value.ordinal }?.key
    }

    operator fun set(
        session: Session,
        status: Account.Status
    ) = synchronized(this) {
        statuses[session.address] = status
    }

    fun getCurrent() = sessions.values.firstOrNull() //sessions[current]

    fun isCurrent(session: Session): Boolean =
        session.address == current

    @InternalCoroutinesApi
    override suspend fun collect(collector: FlowCollector<Session.Status>) =
        broadcast.collect(collector)

    interface Component {
        val sessionManager: SessionManager
    }
}