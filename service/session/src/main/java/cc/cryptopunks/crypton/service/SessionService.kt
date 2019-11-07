package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.FeatureCore
import cc.cryptopunks.crypton.entity.Account.Status.Connecting
import cc.cryptopunks.crypton.selector.SessionsSelector
import cc.cryptopunks.crypton.util.ext.invokeOnClose
import cc.cryptopunks.crypton.util.ext.resolve
import cc.cryptopunks.crypton.util.log
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class SessionService @Inject constructor(
    private val serviceScope: Service.Scope,
    private val selectSessions: SessionsSelector,
    private val createFeatureCore: FeatureCore.Create
) : () -> Job {

    override fun invoke() = serviceScope.launch {
        log<SessionService>("start")
        invokeOnClose { log<SessionService>("stop") }

        selectSessions(Connecting).collect {
            sessionServices.start()
        }
    }

    private val sessionServices
        get() = createFeatureCore()
            .sessionFeature()
            .resolve<SessionServices>()
}