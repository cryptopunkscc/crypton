package cc.cryptopunks.crypton.sys

import android.app.Application
import cc.cryptopunks.crypton.entity.Indicator
import dagger.Binds
import dagger.Component
import dagger.Module

@Suppress("FunctionName")
fun Application.SysIndicator(): Indicator.Sys = DaggerSysIndicator.builder()
    .application(this)
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