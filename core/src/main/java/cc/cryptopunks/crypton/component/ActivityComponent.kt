package cc.cryptopunks.crypton.component

import android.app.Activity
import cc.cryptopunks.crypton.module.ActivityModule
import cc.cryptopunks.crypton.module.ActivityScope
import cc.cryptopunks.crypton.module.ContextModule
import dagger.Component

@ActivityScope
@Component(
    dependencies = [ContextComponent::class],
    modules = [ActivityModule::class]
)
interface ActivityComponent

fun Activity.createActivityComponent(
    applicationComponent: ApplicationComponent
): ActivityComponent = DaggerActivityComponent
    .builder()
    .contextComponent(
        DaggerContextComponent
            .builder()
            .applicationComponent(applicationComponent)
            .contextModule(ContextModule(this))
            .build()
    )
    .build()