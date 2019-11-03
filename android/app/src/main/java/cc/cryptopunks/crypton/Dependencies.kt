package cc.cryptopunks.crypton

import android.app.Application
import androidx.arch.core.executor.ArchTaskExecutor
import cc.cryptopunks.crypton.activity.MainActivity
import cc.cryptopunks.crypton.component.AndroidCore
import cc.cryptopunks.crypton.component.DaggerAndroidCore
import cc.cryptopunks.crypton.core.CoreModule
import cc.cryptopunks.crypton.module.RepoModule
import cc.cryptopunks.crypton.net.NetModule
import cc.cryptopunks.crypton.smack.SmackClientFactory
import cc.cryptopunks.crypton.sys.MessageSys
import cc.cryptopunks.crypton.sys.SysIndicator
import cc.cryptopunks.crypton.sys.SysModule
import cc.cryptopunks.crypton.util.BroadcastError
import cc.cryptopunks.crypton.util.ExecutorsModule
import cc.cryptopunks.crypton.util.IOExecutor
import cc.cryptopunks.crypton.util.MainExecutor

class Dependencies(
    private val application: Application
) {

    private val mainActivityClass = MainActivity::class.java

    private val broadcastErrorComponent = BroadcastError.Module()

    private val repoComponent by lazy {
        RepoModule(application)
    }

    private val createNet by lazy {
        SmackClientFactory {
            copy(
//                securityMode = Net.Factory.Config.SecurityMode.disabled,
//                hostAddress = "10.0.2.2",
                resource = "xmpptest"
            )
        }
    }

    private val core by lazy {
        CoreModule(
            broadcastErrorComponent = broadcastErrorComponent,
            executorsComponent = ExecutorsModule(
                mainExecutor = MainExecutor(ArchTaskExecutor.getMainThreadExecutor()),
                ioExecutor = IOExecutor(ArchTaskExecutor.getIOThreadExecutor())
            ),
            netComponent = NetModule(
                createNet = createNet,
                mapException = ExceptionMapper
            ),
            repo = repoComponent,
            sys = SysModule(
                message = application.MessageSys(mainActivityClass),
                indicator = application.SysIndicator()
            )
        )
    }

    val androidCore: AndroidCore by lazy {
        DaggerAndroidCore.builder()
            .core(core)
            .application(application)
            .module(AndroidCore.Module(mainActivityClass))
            .build()!!
    }
}