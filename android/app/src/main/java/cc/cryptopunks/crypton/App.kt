package cc.cryptopunks.crypton

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import cc.cryptopunks.crypton.smack.initSmack
import cc.cryptopunks.crypton.util.ActivityLifecycleLogger
import cc.cryptopunks.crypton.util.initAndroidLog

class App :
    Application(),
    AppCore {

    private val dependencies = Dependencies(this)

    override val component get() = dependencies.applicationComponent

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        initAndroidLog()
        initAppDebug()
        registerActivityLifecycleCallbacks(ActivityLifecycleLogger)
        initSmack(cacheDir.resolve(OMEMO_STORE_NAME))
        component.appService()
    }

    private companion object {
        private const val OMEMO_STORE_NAME = "omemo"
    }
}