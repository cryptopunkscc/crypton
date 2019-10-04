package cc.cryptopunks.crypton.dagger

import cc.cryptopunks.crypton.service.AppService
import dagger.Component

@Component(
    modules = [
        DaggerFeatureModule::class,
        DaggerContextModule::class,
        DaggerServiceModule::class
    ]
)
interface IndicatorComponent {
    fun inject(target: AppService)
}