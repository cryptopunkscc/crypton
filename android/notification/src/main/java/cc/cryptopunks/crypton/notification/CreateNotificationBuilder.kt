package cc.cryptopunks.crypton.notification

import android.app.Notification.Builder
import android.content.Context
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.N_MR1

fun Context.notificationBuilder(channelId: String) = when {
    SDK_INT > N_MR1 -> Builder(this, channelId)
    else -> Builder(this)
}