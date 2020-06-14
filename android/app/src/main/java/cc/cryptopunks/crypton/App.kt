package cc.cryptopunks.crypton

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import cc.cryptopunks.crypton.activity.MainActivity
import cc.cryptopunks.crypton.context.AppModule
import cc.cryptopunks.crypton.context.Engine
import cc.cryptopunks.crypton.context.Notification
import cc.cryptopunks.crypton.fragment.AndroidChatNotificationFactory
import cc.cryptopunks.crypton.mock.net.MockConnectionFactory
import cc.cryptopunks.crypton.module.*
import cc.cryptopunks.crypton.service.SessionService
import cc.cryptopunks.crypton.service.initExceptionService
import cc.cryptopunks.crypton.sys.AndroidSys
import cc.cryptopunks.crypton.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor

class App :
    Application(),
    Engine {

    private val mainActivityClass = MainActivity::class

    override val core by lazy {
        AppModule(
            mainClass = mainActivityClass,
            mainExecutor = MainExecutor(Dispatchers.Main.asExecutor()),
            ioExecutor = IOExecutor(Dispatchers.IO.asExecutor()),
            repo = RoomRepo(this),
            sys = AndroidSys(
                application = this,
                notificationFactories = mapOf(
                    Notification.Messages::class to AndroidChatNotificationFactory(
                        context = this,
                        mainActivityClass = mainActivityClass.java
                    )
                )
            ),
            createConnection = MockConnectionFactory(),
            createSessionServices = { sessionCore ->
                listOf(SessionService(sessionCore))
            }
        )
    }

    override fun onCreate() {
        super.onCreate()
        initExceptionService()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        initAndroidLog()
        initAppDebug()
        registerActivityLifecycleCallbacks(ActivityLifecycleLogger)
//        initSmack(cacheDir.resolve(OMEMO_STORE_NAME))
        AppDomainModule(core).appService()
    }

    private companion object {
        private const val OMEMO_STORE_NAME = "omemo"
    }
}
