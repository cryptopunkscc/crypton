package cc.cryptopunks.crypton

import android.app.Activity
import android.app.Application
import android.app.Service
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import cc.cryptopunks.crypton.component.ApplicationComponent
import cc.cryptopunks.crypton.component.CoreComponent
import cc.cryptopunks.crypton.util.ActivityLifecycleLogger
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

val applicationComponent get() = application.component
val coreComponent get() = application.coreComponent

private lateinit var application: CoreApplication

abstract class CoreApplication : Application() {

    abstract val component: ApplicationComponent

    abstract val coreComponent: CoreComponent

    @CallSuper
    override fun onCreate() {
        super.onCreate()
        application = this
        Timber.plant(Timber.DebugTree())
        registerActivityLifecycleCallbacks(ActivityLifecycleLogger)
        logErrors()
    }
    private fun logErrors() = GlobalScope.launch {
        coreComponent.broadcastError.collect { Timber.e(it) }
    }

}

val Service.app get() = application as CoreApplication
val Activity.app get() = application as CoreApplication
val Fragment.app get() = activity!!.app