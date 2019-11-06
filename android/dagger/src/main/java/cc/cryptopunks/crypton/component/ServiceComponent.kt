package cc.cryptopunks.crypton.component

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
    modules = [ServiceComponent.Module::class]
)
interface ServiceComponent :
    ServiceCore,
    IndicatorService.Component {

    @dagger.Module
    class Module(
        @get:Provides
        val service: Service
    )
}

@ApplicationScope
class ServiceComponentFactory @Inject constructor(
    androidCore: AndroidCore
) : ServiceCore.Factory, (Service) -> ServiceCore by { service ->
    DaggerServiceComponent.builder()
        .androidCore(androidCore)
        .module(ServiceComponent.Module(service))
        .build()
} {
    @Module
    interface Binding {
        @Binds
        fun ServiceComponentFactory.serviceComponentFactory(): ServiceCore.Factory
    }
}