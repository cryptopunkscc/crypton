package cc.cryptopunks.crypton.app.service

import android.app.Application
import android.content.Intent
import androidx.core.content.ContextCompat.startForegroundService
import javax.inject.Inject

class StartAppService @Inject constructor(context: Application) : () -> Unit by {
    startForegroundService(context, Intent(context, AppService::class.java))
}