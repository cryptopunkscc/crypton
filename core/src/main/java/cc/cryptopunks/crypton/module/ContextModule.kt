package cc.cryptopunks.crypton.module

import android.app.NotificationManager
import android.content.Context
import androidx.core.content.getSystemService
import cc.cryptopunks.crypton.component.ContextComponent

data class ContextModule(
    override val context: Context
) : ContextComponent {
    override val notificationManager: NotificationManager get() = context.getSystemService()!!
}