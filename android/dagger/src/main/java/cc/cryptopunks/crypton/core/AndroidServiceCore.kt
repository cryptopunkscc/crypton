package cc.cryptopunks.crypton.core

import android.app.Service
import cc.cryptopunks.crypton.context.AndroidService
import cc.cryptopunks.crypton.annotation.FeatureScope
import cc.cryptopunks.crypton.service.IndicatorService
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Inject
import javax.inject.Singleton

@FeatureScope
@Component(
    dependencies = [
        AndroidCore::class
    ],
    modules = [
        AndroidServiceCore.Module::class
    ]
)
interface AndroidServiceCore :
    AndroidService.Core,
    IndicatorService.Core {

    @dagger.Module
    class Module(
        @get:Provides
        val service: Service
    )
}

@Singleton
class AndroidServiceCoreFactory @Inject constructor(
    androidCore: AndroidCore
) : AndroidService.Core.Factory, (Service) -> AndroidService.Core by { service ->
    DaggerAndroidServiceCore.builder()
        .androidCore(androidCore)
        .module(AndroidServiceCore.Module(service))
        .build()
} {
    @Module
    interface Bindings {
        @Binds
        fun AndroidServiceCoreFactory.serviceCoreFactory(): AndroidService.Core.Factory
    }
}