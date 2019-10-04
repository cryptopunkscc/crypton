package cc.cryptopunks.crypton.dagger

import android.app.NotificationManager
import android.content.Context
import androidx.core.content.getSystemService
import dagger.Module
import dagger.Provides
@Module
class DaggerContextModule(

    @get:Provides
    val context: Context
) {
    @Provides
    fun Context.notificationManager(): NotificationManager = getSystemService()!!
}