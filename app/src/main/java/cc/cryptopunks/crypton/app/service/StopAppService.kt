package cc.cryptopunks.crypton.app.service

import android.app.Application
import android.content.Intent
import javax.inject.Inject

class StopAppService @Inject constructor(context: Application) : () -> Unit by {
    context.stopService(Intent(context, AppService::class.java))
}