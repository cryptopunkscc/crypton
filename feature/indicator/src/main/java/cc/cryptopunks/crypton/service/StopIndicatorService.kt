package cc.cryptopunks.crypton.service

import android.app.Application
import android.content.Intent
import javax.inject.Inject

class StopIndicatorService @Inject constructor(context: Application) : () -> Unit by {
    context.stopService(Intent(context, IndicatorService::class.java))
}