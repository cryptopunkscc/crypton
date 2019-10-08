package cc.cryptopunks.crypton

import androidx.arch.core.executor.ArchTaskExecutor
import cc.cryptopunks.crypton.activity.MainActivity
import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.component.ApplicationComponent
import cc.cryptopunks.crypton.component.CoreComponent
import cc.cryptopunks.crypton.module.ApplicationModule
import cc.cryptopunks.crypton.module.ClientModule
import cc.cryptopunks.crypton.module.CoreModule
import cc.cryptopunks.crypton.module.RepoModule
import cc.cryptopunks.crypton.smack.SmackClientFactory
import cc.cryptopunks.crypton.util.ExecutorsModule
import cc.cryptopunks.crypton.util.IOExecutor
import cc.cryptopunks.crypton.util.MainExecutor

class App : CoreApplication() {

    override val component: ApplicationComponent by lazy {
        ApplicationModule(
            application = this,
            mainActivityClass = MainActivity::class.java
        )
    }

    override val coreComponent: CoreComponent by lazy {
        CoreModule(
            executorsComponent = ExecutorsModule(
                mainExecutor = MainExecutor(ArchTaskExecutor.getMainThreadExecutor()),
                ioExecutor = IOExecutor(ArchTaskExecutor.getIOThreadExecutor())
            ),
            repoComponent = RepoModule(
                context = this
            ),
            clientComponent = ClientModule(
                createClient = SmackClientFactory {
                    copy(
                        resource = "xmpptest",
                        hostAddress = "10.0.2.2",
                        securityMode = Client.Factory.Config.SecurityMode.disabled
                    )
                },
                mapException = ExceptionMapper
            )
        )
    }
}