package cc.cryptopunks.crypton

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import cc.cryptopunks.crypton.debug.R

internal fun restart() = setting(R.drawable.ic_baseline_refresh_24, "Restart") {
    val packageManager = context.packageManager
    val intent = packageManager.getLaunchIntentForPackage(context.packageName)
    val componentName = intent!!.component
    val mainIntent = Intent.makeRestartActivityTask(componentName)
    context.startActivity(mainIntent)
    Runtime.getRuntime().exit(0)
}

internal fun Context.clearData() = setting(R.drawable.ic_baseline_clear_24, "Clear data") {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        getSystemService(ActivityManager::class.java).clearApplicationUserData()
    else
        Toast.makeText(
            this@clearData,
            "Clear data supported until ${Build.VERSION_CODES.M}",
            Toast.LENGTH_SHORT
        ).show()
}
