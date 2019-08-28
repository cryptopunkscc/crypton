package cc.cryptopunks.crypton.core.module

import cc.cryptopunks.crypton.core.util.Navigation
import cc.cryptopunks.crypton.core.util.NavigationBus
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Scope


@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class FeatureScope

@Module(includes = [FeatureModule.Bindings::class])
class FeatureModule {

    @Provides
    @FeatureScope
    fun navigationBus(): Navigation.Bus = NavigationBus()

    @Module
    interface Bindings {
        @Binds
        fun navigation(bus: Navigation.Bus): Navigation
    }
}