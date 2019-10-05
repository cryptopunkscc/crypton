package cc.cryptopunks.crypton.service

import android.app.Application
import android.content.Intent
import androidx.core.content.ContextCompat.startForegroundService
import javax.inject.Inject

class StartIndicatorService @Inject constructor(context: Application) : () -> Unit by {
    startForegroundService(context, Intent(context, IndicatorService::class.java))
}