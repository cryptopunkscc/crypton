package cc.cryptopunks.crypton.core

import android.app.Service
import cc.cryptopunks.crypton.ServiceCore
import cc.cryptopunks.crypton.annotation.ApplicationScope
import cc.cryptopunks.crypton.annotation.FeatureScope
import cc.cryptopunks.crypton.service.IndicatorService
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Inject

@FeatureScope
@Component(
    dependencies = [AndroidCore::class],
    modules = [AndroidServiceCore.Module::class]
)
interface AndroidServiceCore :
    ServiceCore,
    IndicatorService.Core {

    @dagger.Module
    class Module(
        @get:Provides
        val service: Service
    )
}

@ApplicationScope
class AndroidServiceCoreFactory @Inject constructor(
    androidCore: AndroidCore
) : ServiceCore.Factory, (Service) -> ServiceCore by { service ->
    DaggerAndroidServiceCore.builder()
        .androidCore(androidCore)
        .module(AndroidServiceCore.Module(service))
        .build()
} {
    @Module
    interface Binding {
        @Binds
        fun AndroidServiceCoreFactory.serviceCoreFactory(): ServiceCore.Factory
    }
}