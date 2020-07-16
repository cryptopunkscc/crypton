package cc.cryptopunks.crypton

import android.content.Intent
import cc.cryptopunks.crypton.debug.R

internal fun restart() = setting(R.drawable.ic_baseline_refresh_24, "Restart") {
    val packageManager = context.packageManager
    val intent = packageManager.getLaunchIntentForPackage(context.packageName)
    val componentName = intent!!.component
    val mainIntent = Intent.makeRestartActivityTask(componentName)
    context.startActivity(mainIntent)
    Runtime.getRuntime().exit(0)
}
