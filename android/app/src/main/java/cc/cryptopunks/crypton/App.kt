package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.smack.initSmack

class App : CoreApplication() {

    private val dependencies = Dependencies(this)

    override val core get() = dependencies.androidCore

    override fun onCreate() {
        super.onCreate()
        initSmack(cacheDir.resolve(OMEMO_STORE_NAME))
        dependencies.appComponent.appService()
    }

    private companion object {
        private const val OMEMO_STORE_NAME = "omemo"
    }
}