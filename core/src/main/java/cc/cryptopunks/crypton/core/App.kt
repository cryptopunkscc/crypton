package cc.cryptopunks.crypton.core

import android.app.Activity
import android.app.Application
import android.app.Service
import androidx.fragment.app.Fragment
import cc.cryptopunks.crypton.core.component.ApplicationComponent
import cc.cryptopunks.crypton.core.component.DaggerApplicationComponent
import cc.cryptopunks.crypton.core.module.ApplicationModule
import cc.cryptopunks.crypton.core.util.ActivityLifecycleLogger
import timber.log.Timber

class App : Application() {

    val component: ApplicationComponent by lazy {
        DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(ActivityLifecycleLogger)
        component
        Timber.plant(Timber.DebugTree())
    }
}

val Service.app get() = application as App
val Activity.app get() = application as App
val Fragment.app get() = activity?.app