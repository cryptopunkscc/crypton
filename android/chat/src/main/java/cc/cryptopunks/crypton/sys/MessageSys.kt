package cc.cryptopunks.crypton.sys

import android.app.Application
import android.app.NotificationManager
import androidx.core.content.getSystemService
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.notification.ShowMessageNotification
import dagger.Binds
import dagger.Component
import dagger.Provides

class MessageSysModule(
    application: Application,
    mainActivityClass: Class<*>
) : Message.Sys by DaggerMessageSys.builder()
    .application(application)
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
        @get:Provides val mainActivityClass: Class<*>
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