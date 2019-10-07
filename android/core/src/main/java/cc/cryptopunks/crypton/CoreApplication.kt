package cc.cryptopunks.crypton

import android.app.Activity
import android.app.Application
import android.app.Service
import androidx.fragment.app.Fragment
import cc.cryptopunks.crypton.component.CoreComponent
import cc.cryptopunks.crypton.component.FeatureComponent
import cc.cryptopunks.crypton.util.ActivityLifecycleLogger
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

abstract class BaseApplication : Application() {

    abstract val component: Component

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        registerActivityLifecycleCallbacks(ActivityLifecycleLogger)
        logErrors()
    }

    interface Component : CoreComponent {
        val application: Application
        val mainActivityClass: Class<out Activity>
        fun featureComponent(): FeatureComponent
    }

    private fun logErrors() = GlobalScope.launch {
        component.broadcastError.collect { Timber.e(it) }
    }
}

val Service.app get() = application as BaseApplication
val Activity.app get() = application as BaseApplication
val Fragment.app get() = activity!!.app