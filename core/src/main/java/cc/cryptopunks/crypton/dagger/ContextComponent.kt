package cc.cryptopunks.crypton.dagger

import android.app.NotificationManager
import cc.cryptopunks.crypton.BaseApplication
import dagger.Component

@Component(
    dependencies = [BaseApplication.Component::class],
    modules = [DaggerContextModule::class]
)
interface ContextComponent : BaseApplication.Component {
    val notificationManager: NotificationManager
}