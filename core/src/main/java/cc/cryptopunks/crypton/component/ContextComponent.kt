package cc.cryptopunks.crypton.component

import android.app.NotificationManager
import cc.cryptopunks.crypton.module.ContextModule
import cc.cryptopunks.crypton.module.ContextScope
import dagger.Component

@ContextScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [ContextModule::class]
)
interface ContextComponent : ApplicationComponent {
    val notificationManager: NotificationManager
}