package cc.cryptopunks.crypton

import android.app.Application
import cc.cryptopunks.crypton.activity.MainActivity
import cc.cryptopunks.crypton.core.AndroidCore
import cc.cryptopunks.crypton.core.ApplicationCore
import cc.cryptopunks.crypton.core.DaggerAndroidCore
import cc.cryptopunks.crypton.core.DaggerApplicationCore
import cc.cryptopunks.crypton.module.RepoModule
import cc.cryptopunks.crypton.smack.SmackConnectionFactory

class Dependencies(
    private val application: Application
) {
    private val mainActivityClass
        get() =  MainActivity::class.java

    private val repoCore
        get() = RepoModule(application)

    private val createSmackConnection
        get() = SmackConnectionFactory {
            copy(
//                securityMode = Net.Factory.Config.SecurityMode.disabled,
//                hostAddress = "10.0.2.2",
                resource = "xmpptest"
            )
        }

    private val androidCore
        get() = DaggerAndroidCore.builder()
            .application(application)
            .repo(repoCore)
            .module(AndroidCore.Module(
                mainActivityClass = mainActivityClass,
                createConnection = createSmackConnection
            ))
            .build()!!

    val applicationCore: ApplicationCore by lazy {
        DaggerApplicationCore.builder()
            .androidCore(androidCore)
            .build()
    }
}