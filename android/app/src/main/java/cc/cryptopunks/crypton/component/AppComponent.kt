package cc.cryptopunks.crypton.component

import cc.cryptopunks.crypton.module.AppBindings
import cc.cryptopunks.crypton.service.AppService
import dagger.Component
import javax.inject.Scope

@Scope
annotation class AppScope

@AppScope
@Component(
    dependencies = [
        AndroidCore::class
    ],
    modules = [
        AppBindings::class
    ]
)
interface AppComponent :
    AndroidCore,
    AppService.Component