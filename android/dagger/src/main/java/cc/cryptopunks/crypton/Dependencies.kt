package cc.cryptopunks.crypton

import android.app.Application
import cc.cryptopunks.crypton.activity.MainActivity
import cc.cryptopunks.crypton.core.AndroidCore
import cc.cryptopunks.crypton.core.DaggerAndroidCore
import cc.cryptopunks.crypton.module.RepoModule
import cc.cryptopunks.crypton.smack.SmackConnectionFactory

class Dependencies(
    private val application: Application
) {
    private val mainActivityClass =
        MainActivity::class.java

    private val repo by lazy {
        RepoModule(application)
    }

    private val createSmackConnection by lazy {
        SmackConnectionFactory {
            copy(
//                securityMode = Net.Factory.Config.SecurityMode.disabled,
//                hostAddress = "10.0.2.2",
                resource = "xmpptest"
            )
        }
    }

    val androidCore by lazy {
        DaggerAndroidCore.builder()
            .application(application)
            .repo(repo)
            .module(
                AndroidCore.Module(
                    mainActivityClass = mainActivityClass,
                    createConnection = createSmackConnection
                )
            )
            .build()!!
    }
}