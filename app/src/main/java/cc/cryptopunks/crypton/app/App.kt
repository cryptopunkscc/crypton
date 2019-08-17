package cc.cryptopunks.crypton.app

import android.app.Activity
import android.app.Application
import android.app.Service
import androidx.fragment.app.Fragment
import cc.cryptopunks.crypton.app.module.ApplicationModule
import cc.cryptopunks.crypton.app.module.CoreModule
import cc.cryptopunks.crypton.app.module.DatabaseModule
import timber.log.Timber

class App : Application() {

    val component: AppComponent by lazy {
        DaggerAppComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .coreModule(CoreModule())
            .databaseModule(DatabaseModule())
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        component
        Timber.plant(Timber.DebugTree())
    }
}

val Service.app get() = application as App
val Activity.app get() = application as App
val Fragment.app get() = activity?.app