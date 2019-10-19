package cc.cryptopunks.crypton.sys

import android.app.Application
import android.content.Intent
import cc.cryptopunks.crypton.entity.Indicator
import cc.cryptopunks.crypton.service.IndicatorService
import javax.inject.Inject

internal class StopIndicatorService @Inject constructor(
    context: Application
) : Indicator.Sys.Hide, () -> Unit by {
    context.stopService(Intent(context, IndicatorService::class.java))
}