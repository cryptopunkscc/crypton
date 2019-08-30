package cc.cryptopunks.crypton.util

import android.app.Activity
import android.app.Application
import android.os.Bundle
import timber.log.Timber

object ActivityLifecycleLogger : Application.ActivityLifecycleCallbacks {
    override fun onActivityPaused(activity: Activity) {
        activity log "onPaused"
    }

    override fun onActivityResumed(activity: Activity) {
        activity log "onResumed"
    }

    override fun onActivityStarted(activity: Activity) {
        activity log "onStarted"
    }

    override fun onActivityDestroyed(activity: Activity) {
        activity log "onDestroyed"
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {
        activity log "onSaveInstanceState"
    }

    override fun onActivityStopped(activity: Activity) {
        activity log "onStopped"
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        activity log "onCreated"
    }

    private infix fun Activity.log(message: String) {
        Timber.d("${javaClass.simpleName}: $message")
    }
}