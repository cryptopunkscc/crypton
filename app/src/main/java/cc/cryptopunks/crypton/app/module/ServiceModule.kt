package cc.cryptopunks.crypton.app.module

import android.app.Service
import cc.cryptopunks.crypton.common.HandleError
import cc.cryptopunks.crypton.app.util.AsyncExecutor
import cc.cryptopunks.crypton.app.util.RunningTasks
import dagger.Module
import dagger.Provides
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ServiceScope

@Module
class ServiceModule(
    private val service: Service
) {

    @Provides
    @ServiceScope
    fun service(): Service = service

    @Provides
    @ServiceScope
    fun asyncExecutor(
        handleError: HandleError,
        runningTasks: RunningTasks
    ) = AsyncExecutor(
        handleError = handleError,
        runningTasks = runningTasks
    )
}