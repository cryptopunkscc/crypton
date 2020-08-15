package cc.cryptopunks.crypton.debug.drawer

import android.app.ActivityManager
import android.content.Intent
import android.os.Build
import android.widget.Toast
import cc.cryptopunks.crypton.activity.BaseActivity
import cc.cryptopunks.crypton.debug.R

internal fun restart() = setting(
    R.drawable.ic_baseline_refresh_24,
    "Restart"
) {
    val packageManager = context.packageManager
    val intent = packageManager.getLaunchIntentForPackage(context.packageName)
    val componentName = intent!!.component
    val mainIntent = Intent.makeRestartActivityTask(componentName)
    context.startActivity(mainIntent)
    Runtime.getRuntime().exit(0)
}

internal fun clearData() = setting(
    R.drawable.ic_baseline_clear_24,
    "Clear data"
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        context.getSystemService(ActivityManager::class.java).clearApplicationUserData()
    else
        Toast.makeText(
            context,
            "Clear data supported until ${Build.VERSION_CODES.M}",
            Toast.LENGTH_SHORT
        ).show()
}

internal fun DebugWrapper.debugView() = setting(
    R.drawable.ic_account_circle_white_24dp,
    "Debug"
) {
    closeDrawer()
    (context as BaseActivity).navController.navigate(R.id.navigateDebug)
}
