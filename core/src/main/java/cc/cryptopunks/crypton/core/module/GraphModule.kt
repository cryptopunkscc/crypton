package cc.cryptopunks.crypton.core.module

import cc.cryptopunks.crypton.core.util.Navigation
import cc.cryptopunks.crypton.core.util.NavigationBus
import dagger.Module
import dagger.Provides
import javax.inject.Scope


@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class GraphScope

@Module
class GraphModule {

    @Provides
    @GraphScope
    fun navigationBus(): Navigation.Bus = NavigationBus()
}