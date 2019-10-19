package cc.cryptopunks.crypton.sys

import android.app.Application
import android.content.Intent
import androidx.core.content.ContextCompat.startForegroundService
import cc.cryptopunks.crypton.entity.Indicator
import cc.cryptopunks.crypton.service.IndicatorService
import javax.inject.Inject

internal class StartIndicatorService @Inject constructor(
    context: Application
) : Indicator.Sys.Show, () -> Unit by {
    startForegroundService(context, Intent(context, IndicatorService::class.java))
}