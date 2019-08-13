package cc.cryptopunks.crypton.app.app

import android.app.Application
import cc.cryptopunks.crypton.app.module.ApplicationModule
import timber.log.Timber

class App : Application() {

    val component: AppComponent by lazy {
        cc.cryptopunks.crypton.app.app.DaggerAppComponent.builder()
            .applicationModule(ApplicationModule(this))
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        component
        Timber.plant(Timber.DebugTree())
    }
}