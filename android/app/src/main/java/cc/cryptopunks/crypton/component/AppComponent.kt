package cc.cryptopunks.crypton.component

import cc.cryptopunks.crypton.core.Core
import cc.cryptopunks.crypton.core.service.CoreService
import dagger.Component

@Component(dependencies = [Core.Component::class])
interface AppComponent {
    val coreService: CoreService
}