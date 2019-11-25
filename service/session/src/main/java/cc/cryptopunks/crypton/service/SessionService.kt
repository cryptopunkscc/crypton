package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.FeatureCore
import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.selector.SessionEventSelector
import cc.cryptopunks.crypton.util.ext.invokeOnClose
import cc.cryptopunks.crypton.util.ext.resolve
import cc.cryptopunks.crypton.util.log
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import javax.inject.Inject

class SessionService @Inject constructor(
    private val serviceScope: Service.Scope,
    private val selectSessionEvent: SessionEventSelector,
    private val createFeatureCore: FeatureCore.Create
) : () -> Job {

    override fun invoke() = serviceScope.launch {
        log<SessionService>("start")
        invokeOnClose { log<SessionService>("stop") }

        selectSessionEvent()
            .filter { it.event is Account.Event.Authenticated }
            .collect { sessionServices.start() }
    }

    private val sessionServices
        get() = createFeatureCore()
            .sessionFeature()
            .resolve<SessionServices>()
}