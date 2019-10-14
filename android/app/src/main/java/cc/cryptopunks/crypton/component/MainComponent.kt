package cc.cryptopunks.crypton.component

import cc.cryptopunks.crypton.fragment.MainFragment
import cc.cryptopunks.crypton.navigation.Navigation
import dagger.Component

@Component(
    dependencies = [
        ApplicationComponent::class,
        Navigation.Component::class
    ]
)
interface MainComponent {
    fun inject(target: MainFragment)

    @Component.Factory
    interface Factory {
        fun create(
            application: ApplicationComponent,
            navigation: Navigation.Component
        ): MainComponent
    }
}