package cc.cryptopunks.crypton

import android.app.Application
import cc.cryptopunks.crypton.smack.initSmack
import cc.cryptopunks.crypton.util.ActivityLifecycleLogger
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class App :
    Application(),
    AppCore {

    private val dependencies = Dependencies(this)

    override val component get() = dependencies.applicationComponent

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        initAppDebug()
        registerActivityLifecycleCallbacks(ActivityLifecycleLogger)
        logErrors()
        initSmack(cacheDir.resolve(OMEMO_STORE_NAME))
        component.appService()
    }

    private fun logErrors() = GlobalScope.launch {
        component.broadcastError.collect { Timber.e(it) }
    }

    private companion object {
        private const val OMEMO_STORE_NAME = "omemo"
    }
}