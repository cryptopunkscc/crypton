package cc.cryptopunks.crypton.app.service

import android.app.Activity
import android.content.Intent
import cc.cryptopunks.crypton.common.ActivityScope
import javax.inject.Inject

@ActivityScope
class StopAppService @Inject constructor(activity: Activity) : () -> Unit by {
    activity.stopService(Intent(activity, AppService::class.java))
}