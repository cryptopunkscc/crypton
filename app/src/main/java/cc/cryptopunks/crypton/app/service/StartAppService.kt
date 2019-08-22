package cc.cryptopunks.crypton.app.service

import android.app.Activity
import android.content.Intent
import androidx.core.content.ContextCompat.startForegroundService
import cc.cryptopunks.crypton.common.ActivityScope
import javax.inject.Inject

@ActivityScope
class StartAppService @Inject constructor(activity: Activity) : () -> Unit by {
    startForegroundService(activity, Intent(activity, AppService::class.java))
}