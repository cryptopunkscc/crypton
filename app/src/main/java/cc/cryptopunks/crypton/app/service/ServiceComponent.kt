package cc.cryptopunks.crypton.app.service

import cc.cryptopunks.crypton.app.module.ServiceModule
import cc.cryptopunks.crypton.app.module.ServiceScope
import dagger.Subcomponent

@ServiceScope
@Subcomponent(modules = [ServiceModule::class])
interface ServiceComponent