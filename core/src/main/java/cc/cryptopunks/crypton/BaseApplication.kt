package cc.cryptopunks.crypton

import android.app.Activity
import android.app.Application
import android.app.Service
import androidx.fragment.app.Fragment
import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.component.FeatureComponent
import cc.cryptopunks.crypton.repo.Repo
import cc.cryptopunks.crypton.util.ActivityLifecycleLogger
import cc.cryptopunks.crypton.util.BroadcastError
import cc.cryptopunks.crypton.util.Scopes
import timber.log.Timber

abstract class BaseApplication : Application() {

    abstract val component: Component

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        registerActivityLifecycleCallbacks(ActivityLifecycleLogger)
        component
    }

    interface Component :
        Repo.Component,
        Client.Component,
        BroadcastError.Component {

        val application: Application

        val mainActivityClass: Class<out Activity>

        val useCaseScope: Scopes.UseCase

        fun featureComponent(): FeatureComponent
    }
}

val Service.app get() = application as BaseApplication
val Activity.app get() = application as BaseApplication
val Fragment.app get() = activity?.app