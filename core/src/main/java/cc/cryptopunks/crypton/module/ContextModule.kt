package cc.cryptopunks.crypton.module

import android.app.NotificationManager
import android.content.Context
import androidx.core.content.getSystemService
import dagger.Module
import dagger.Provides
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ContextScope

@Module
class ContextModule(

    @get:Provides
    @get:ContextScope
    val context: Context
) {
    @Provides
    @ContextScope
    fun Context.notificationManager(): NotificationManager = getSystemService()!!
}