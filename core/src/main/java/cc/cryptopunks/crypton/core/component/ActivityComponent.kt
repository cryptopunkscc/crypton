package cc.cryptopunks.crypton.core.component

import cc.cryptopunks.crypton.core.module.ActivityModule
import cc.cryptopunks.crypton.common.ActivityScope
import dagger.Component

@ActivityScope
@Component(
    dependencies = [ContextComponent::class],
    modules = [ActivityModule::class]
)
interface ActivityComponent
