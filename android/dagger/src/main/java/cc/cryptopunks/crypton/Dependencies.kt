package cc.cryptopunks.crypton

import android.app.Application
import androidx.arch.core.executor.ArchTaskExecutor
import cc.cryptopunks.crypton.activity.MainActivity
import cc.cryptopunks.crypton.core.AndroidCore
import cc.cryptopunks.crypton.core.ApplicationCore
import cc.cryptopunks.crypton.core.DaggerAndroidCore
import cc.cryptopunks.crypton.core.DaggerApplicationCore
import cc.cryptopunks.crypton.context.Connection
import cc.cryptopunks.crypton.context.Core
import cc.cryptopunks.crypton.context.Sys
import cc.cryptopunks.crypton.module.RepoModule
import cc.cryptopunks.crypton.smack.SmackConnectionFactory
import cc.cryptopunks.crypton.sys.MessageSysModule
import cc.cryptopunks.crypton.sys.SysIndicatorModule
import cc.cryptopunks.crypton.util.*

class Dependencies(
    private val application: Application
) {
    private val mainActivityClass = MainActivity::class.java
    private val broadcastErrorCore = BroadcastError.Module()

    private val repoCore by lazy {
        RepoModule(application)
    }

    private val createNet by lazy {
        SmackConnectionFactory {
            copy(
//                securityMode = Net.Factory.Config.SecurityMode.disabled,
//                hostAddress = "10.0.2.2",
                resource = "xmpptest"
            )
        }
    }

    private val core by lazy {
        Core.Module(
            broadcastErrorCore = broadcastErrorCore,
            executorsCore = Executors.Module(
                mainExecutor = MainExecutor(ArchTaskExecutor.getMainThreadExecutor()),
                ioExecutor = IOExecutor(ArchTaskExecutor.getIOThreadExecutor())
            ),
            repo = repoCore,
            connectionCore = Connection.Module(
                createConnection = createNet
            ),
            sys = Sys.Module(
                message = MessageSysModule(
                    application = application,
                    mainActivityClass = mainActivityClass
                ),
                indicator = SysIndicatorModule(
                    application = application
                )
            )
        )
    }

    private val androidCore: AndroidCore by lazy {
        DaggerAndroidCore.builder()
            .core(core)
            .application(application)
            .module(AndroidCore.Module(mainActivityClass))
            .build()!!
    }

    val applicationCore: ApplicationCore by lazy {
        DaggerApplicationCore.builder()
            .androidCore(androidCore)
            .build()
    }
}