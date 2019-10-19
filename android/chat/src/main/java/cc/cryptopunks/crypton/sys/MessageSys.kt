package cc.cryptopunks.crypton.sys

import android.app.Activity
import android.app.Application
import android.app.NotificationManager
import androidx.core.content.getSystemService
import cc.cryptopunks.crypton.entity.Message
import cc.cryptopunks.crypton.notification.ShowMessageNotification
import dagger.Binds
import dagger.Component
import dagger.Provides

@Suppress("FunctionName")
fun Application.MessageSys(
    mainActivityClass: Class<out Activity>
): Message.Sys = DaggerMessageSys.builder()
    .application(this)
    .module(MessageSys.Module(mainActivityClass))
    .build()

@Component(
    dependencies = [Application::class],
    modules = [
        MessageSys.Module::class,
        MessageSys.Bindings::class
    ]
)
internal interface MessageSys : Message.Sys {

    @dagger.Module
    class Module(
        @get:Provides val mainActivityClass: Class<out Activity>
    ) {
        @Provides
        fun Application.notificationManager(): NotificationManager = getSystemService()!!
    }

    @dagger.Module
    abstract class Bindings {
        @Binds
        abstract fun ShowMessageNotification.bind(): Message.Sys.ShowNotification
    }
}