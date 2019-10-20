package cc.cryptopunks.crypton

import androidx.arch.core.executor.ArchTaskExecutor
import cc.cryptopunks.crypton.activity.MainActivity
import cc.cryptopunks.crypton.api.ClientModule
import cc.cryptopunks.crypton.component.ApplicationComponent
import cc.cryptopunks.crypton.core.CoreModule
import cc.cryptopunks.crypton.core.service.DaggerCoreService_Component
import cc.cryptopunks.crypton.module.ApplicationModule
import cc.cryptopunks.crypton.module.RepoModule
import cc.cryptopunks.crypton.repo.RepoProvider
import cc.cryptopunks.crypton.smack.SmackClientFactory
import cc.cryptopunks.crypton.smack.initSmack
import cc.cryptopunks.crypton.sys.MessageSys
import cc.cryptopunks.crypton.sys.SysIndicator
import cc.cryptopunks.crypton.sys.SysModule
import cc.cryptopunks.crypton.util.BroadcastError
import cc.cryptopunks.crypton.util.ExecutorsModule
import cc.cryptopunks.crypton.util.IOExecutor
import cc.cryptopunks.crypton.util.MainExecutor

class App : CoreApplication() {

    private val broadcastErrorComponent = BroadcastError.Module()

    private val repoComponent by lazy { RepoModule(this) }

    override val component: ApplicationComponent by lazy {
        ApplicationModule(
            application = this,
            mainActivityClass = MainActivity::class.java,
            coreModule = CoreModule(
                broadcastErrorComponent = broadcastErrorComponent,
                executorsComponent = ExecutorsModule(
                    mainExecutor = MainExecutor(ArchTaskExecutor.getMainThreadExecutor()),
                    ioExecutor = IOExecutor(ArchTaskExecutor.getIOThreadExecutor())
                ),
                clientComponent = ClientModule(
                    createApi = SmackClientFactory(
                        broadcastError = broadcastErrorComponent.broadcastError
                    ) {
                        copy(
//                            securityMode = Client.Factory.Config.SecurityMode.disabled,
//                            hostAddress = "10.0.2.2",
                            resource = "xmpptest"
                        )
                    },
                    mapException = ExceptionMapper
                ),
                repoComponent = repoComponent,
                sysComponent = SysModule(
                    message = MessageSys(MainActivity::class.java),
                    indicator = SysIndicator()
                ),
                repoProvider = RepoProvider(repoComponent)
            )
        )
    }

    override fun onCreate() {
        super.onCreate()
        initSmack(cacheDir.resolve(OMEMO_STORE_NAME))
        DaggerCoreService_Component.builder()
            .component(component)
            .build()
            .coreService()
    }

    private companion object {
        private const val OMEMO_STORE_NAME = "omemo"
    }
}