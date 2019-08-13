package cc.cryptopunks.crypton.app.ui.component

import cc.cryptopunks.crypton.common.ActivityScope
import cc.cryptopunks.crypton.app.module.ActivityModule
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface ActivityComponent
