package cc.cryptopunks.crypton.app.service

import cc.cryptopunks.crypton.app.ContextComponent
import cc.cryptopunks.crypton.app.module.ServiceModule
import cc.cryptopunks.crypton.app.module.ServiceScope
import dagger.Component

@ServiceScope
@Component(
    dependencies = [ContextComponent::class],
    modules = [ServiceModule::class]
)
interface ServiceComponent : ContextComponent {
    fun inject(xmppService: XmppService)
}