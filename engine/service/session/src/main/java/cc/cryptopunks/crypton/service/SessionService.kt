package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.Service
import cc.cryptopunks.crypton.core.FeatureCore
import cc.cryptopunks.crypton.selector.SessionEventSelector
import cc.cryptopunks.crypton.util.ext.invokeOnClose
import cc.cryptopunks.crypton.util.ext.resolve
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionService @Inject constructor(
    private val serviceScope: Service.Scope,
    private val selectSessionEvent: SessionEventSelector,
    private val createFeatureCore: FeatureCore.Create
) : () -> Job {

    private val log = typedLog()

    override fun invoke() = serviceScope.launch {
        log.d("start")
        invokeOnClose { log.d("stop") }
        selectSessionEvent().collect { event ->
            sessionServices(event)
        }
    }

    private val sessionServices by lazy {
        createFeatureCore()
            .sessionFeature()
            .resolve<SessionServices>()
    }
}