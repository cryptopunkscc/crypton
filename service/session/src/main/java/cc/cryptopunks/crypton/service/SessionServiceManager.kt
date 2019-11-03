package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.entity.Account.Status.Connected
import cc.cryptopunks.crypton.factory.SessionServicesFactory
import cc.cryptopunks.crypton.selector.SessionsSelector
import cc.cryptopunks.crypton.util.ext.invokeOnClose
import cc.cryptopunks.crypton.util.log
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class SessionServiceManager @Inject constructor(
    private val serviceScope: Service.Scope,
    private val selectSessions: SessionsSelector,
    private val createSessionServices: SessionServicesFactory
) : () -> Job {

    override fun invoke() = serviceScope.launch {
        log<SessionServiceManager>("start")
        invokeOnClose { log<SessionServiceManager>("stop") }
        selectSessions(Connected).collect { session ->
            createSessionServices(session).start()
        }
    }
}