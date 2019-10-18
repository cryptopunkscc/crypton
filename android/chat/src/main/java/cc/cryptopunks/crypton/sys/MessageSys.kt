package cc.cryptopunks.crypton.sys

import android.app.Application
import cc.cryptopunks.crypton.entity.Message
import cc.cryptopunks.crypton.notification.ShowMessageNotification
import dagger.Binds
import dagger.Component
import dagger.Module

@Suppress("FunctionName")
fun Application.MessageSys(): Message.Sys = DaggerMessageSys.builder()
    .application(this)
    .build()

@Component(
    dependencies = [Application::class],
    modules = [MessageSys.Bindings::class]
)
internal interface MessageSys : Message.Sys {
    @Module
    interface Bindings {
        @Binds
        fun ShowMessageNotification.bind(): Message.Sys.ShowNotification
    }
}