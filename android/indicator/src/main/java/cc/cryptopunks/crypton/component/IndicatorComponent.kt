package cc.cryptopunks.crypton.component

import cc.cryptopunks.crypton.service.IndicatorService
import dagger.Component

@Component(
    dependencies = [
        FeatureComponent::class,
        ServiceComponent::class
    ]
)
interface IndicatorComponent {
    fun inject(target: IndicatorService)
}