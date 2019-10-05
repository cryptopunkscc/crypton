package cc.cryptopunks.crypton.component

import android.app.NotificationManager
import android.content.Context

interface ContextComponent {
    val context: Context
    val notificationManager: NotificationManager
}