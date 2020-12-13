package cc.cryptopunks.crypton.feature

import cc.cryptopunks.crypton.connector
import cc.cryptopunks.crypton.context.RootScope
import cc.cryptopunks.crypton.context.Subscribe
import cc.cryptopunks.crypton.createEmitters
import cc.cryptopunks.crypton.feature
import cc.cryptopunks.crypton.features
import cc.cryptopunks.crypton.interactor.loadSessions
import cc.cryptopunks.crypton.service
import cc.cryptopunks.crypton.serviceName
import cc.cryptopunks.crypton.util.Log
import cc.cryptopunks.crypton.util.logger.CoroutineLog
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flowOn

internal fun startAppServices() = feature(

    handler = { _, _: Subscribe.AppService ->
        println("Start AppService")

        loadSessions()

        val context = coroutineContext
            .minusKey(Job)
            .plus(CoroutineLog.Label("RootEmitter"))
            .plus(CoroutineLog.Status(Log.Event.Status.Handling))

        val emitters = createEmitters<RootScope>(features)
            .flowOn(context)
            .connector()

        service("Root".serviceName)
            .run { emitters.connect() }
            .join()

        println("Finish AppService")
    }
)
