package cc.cryptopunks.crypton

import android.app.Application
import androidx.annotation.CallSuper
import cc.cryptopunks.crypton.component.ApplicationComponent
import cc.cryptopunks.crypton.core.Core
import cc.cryptopunks.crypton.util.ActivityLifecycleLogger
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

val applicationComponent: ApplicationComponent get() = INSTANCE.component

val coreComponent: Core.Component get() = INSTANCE.component

private lateinit var INSTANCE: CoreApplication

abstract class CoreApplication : Application() {

    abstract val component: ApplicationComponent

    @CallSuper
    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        Timber.plant(Timber.DebugTree())
        registerActivityLifecycleCallbacks(ActivityLifecycleLogger)
        logErrors()
    }
    private fun logErrors() = GlobalScope.launch {
        component.broadcastError.collect { Timber.e(it) }
    }

}