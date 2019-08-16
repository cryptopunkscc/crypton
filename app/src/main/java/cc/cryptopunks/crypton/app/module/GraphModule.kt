package cc.cryptopunks.crypton.app.module

import cc.cryptopunks.crypton.app.util.Navigation
import cc.cryptopunks.crypton.app.util.NavigationBus
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