package cc.cryptopunks.crypton.sys

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import cc.cryptopunks.crypton.context.Indicator
import cc.cryptopunks.crypton.service.IndicatorService

internal class IndicatorSys(
    private val context: Context
) : Indicator.Sys {

    override val isIndicatorVisible: Boolean get() = IndicatorService.isRunning

    override fun showIndicator() {
        ContextCompat.startForegroundService(context, Intent(context, IndicatorService::class.java))
    }

    override fun hideIndicator() {
        context.stopService(Intent(context, IndicatorService::class.java))
    }
}
