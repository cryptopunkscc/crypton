package cc.cryptopunks.crypton

import android.app.Application
import androidx.annotation.CallSuper
import cc.cryptopunks.crypton.component.AndroidCore
import cc.cryptopunks.crypton.core.Core
import cc.cryptopunks.crypton.util.ActivityLifecycleLogger
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

val core: Core get() = INSTANCE.core
val androidCore: AndroidCore get() = INSTANCE.core

private lateinit var INSTANCE: CoreApplication

abstract class CoreApplication : Application() {

    abstract val core: AndroidCore

    @CallSuper
    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        Timber.plant(Timber.DebugTree())
        registerActivityLifecycleCallbacks(ActivityLifecycleLogger)
        logErrors()
    }
    private fun logErrors() = GlobalScope.launch {
        core.broadcastError.collect { Timber.e(it) }
    }
}