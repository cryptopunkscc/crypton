package cc.cryptopunks.crypton.sys

import android.app.Application
import cc.cryptopunks.crypton.context.Indicator
import dagger.Binds
import dagger.Component
import dagger.Module
import javax.inject.Inject

class SysIndicatorModule @Inject constructor(
    application: Application
): Indicator.Sys by DaggerSysIndicator.builder()
    .application(application)
    .build()

@Component(
    dependencies = [Application::class],
    modules = [SysIndicator.Bindings::class]
)
internal interface SysIndicator : Indicator.Sys {
    @Module
    interface Bindings {
        @Binds
        fun StartIndicatorService.start(): Indicator.Sys.Show

        @Binds
        fun StopIndicatorService.stop(): Indicator.Sys.Hide
    }
}