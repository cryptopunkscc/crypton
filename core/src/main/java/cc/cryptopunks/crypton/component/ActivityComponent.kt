package cc.cryptopunks.crypton.component

import cc.cryptopunks.crypton.module.ActivityModule
import cc.cryptopunks.crypton.util.ActivityScope
import dagger.Component

@ActivityScope
@Component(
    dependencies = [ContextComponent::class],
    modules = [ActivityModule::class]
)
interface ActivityComponent
