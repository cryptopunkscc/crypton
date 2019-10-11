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
interface AppComponent {
    fun inject(target: MainFragment)
}