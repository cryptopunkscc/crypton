package cc.cryptopunks.crypton.core.component

import android.app.NotificationManager
import cc.cryptopunks.crypton.core.module.ContextModule
import cc.cryptopunks.crypton.core.module.ContextScope
import dagger.Component

@ContextScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [ContextModule::class]
)
interface ContextComponent : ApplicationComponent {
    val notificationManager: NotificationManager
}