package cc.cryptopunks.crypton.app.ui.component

import cc.cryptopunks.crypton.app.ContextComponent
import cc.cryptopunks.crypton.app.module.ActivityModule
import cc.cryptopunks.crypton.common.ActivityScope
import dagger.Component

@ActivityScope
@Component(
    dependencies = [ContextComponent::class],
    modules = [ActivityModule::class]
)
interface ActivityComponent
