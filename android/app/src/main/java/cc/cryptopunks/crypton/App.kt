package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.component.AndroidCore
import cc.cryptopunks.crypton.service.AppService
import cc.cryptopunks.crypton.smack.initSmack
import javax.inject.Scope

class App : CoreApplication() {

    @Scope
    annotation class AppScope

    @AppScope
    @dagger.Component(
        dependencies = [AndroidCore::class]
    )
    interface Component : AppService.Component

    private val dependencies = Dependencies(this)

    override val core get() = dependencies.androidCore

    override fun onCreate() {
        super.onCreate()
        initSmack(cacheDir.resolve(OMEMO_STORE_NAME))
        DaggerApp_Component.builder()
            .androidCore(dependencies.androidCore)
            .build()
            .appService()
    }

    private companion object {
        private const val OMEMO_STORE_NAME = "omemo"
    }
}