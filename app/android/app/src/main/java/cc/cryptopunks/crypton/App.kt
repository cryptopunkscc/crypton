package cc.cryptopunks.crypton

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import cc.cryptopunks.crypton.activity.MainActivity
import cc.cryptopunks.crypton.context.RootModule
import cc.cryptopunks.crypton.context.Core
import cc.cryptopunks.crypton.context.Notification
import cc.cryptopunks.crypton.context.Subscribe
import cc.cryptopunks.crypton.debug.drawer.initAppDebug
import cc.cryptopunks.crypton.fragment.AndroidChatNotificationFactory
import cc.cryptopunks.crypton.room.RoomAppRepo
import cc.cryptopunks.crypton.navigate.currentAccount
import cc.cryptopunks.crypton.selector.newSessionsFlow
import cc.cryptopunks.crypton.service.cryptonFeatures
import cc.cryptopunks.crypton.service.initExceptionService
import cc.cryptopunks.crypton.smack.SmackConnectionFactory
import cc.cryptopunks.crypton.smack.initSmack
import cc.cryptopunks.crypton.sys.AndroidSys
import cc.cryptopunks.crypton.util.ActivityLifecycleLogger
import cc.cryptopunks.crypton.util.IOExecutor
import cc.cryptopunks.crypton.util.MainExecutor
import cc.cryptopunks.crypton.util.initAndroidLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class App :
    Application(),
    Core {

    private val mainActivityClass = MainActivity::class

    override val scope by lazy {
        RootModule(
            mainClass = mainActivityClass,
            mainExecutor = MainExecutor(Dispatchers.Main.asExecutor()),
            ioExecutor = IOExecutor(Dispatchers.IO.asExecutor()),
            repo = RoomAppRepo(this),
            sys = AndroidSys(
                application = this,
                notificationFactories = mapOf(
                    Notification.Messages::class to AndroidChatNotificationFactory(
                        context = this,
                        mainActivityClass = mainActivityClass.java,
                        navGraphId = R.navigation.main
                    )
                ),
                appNameResId = R.string.app_name,
                smallIconResId = R.mipmap.ic_launcher_round
            ),
            createConnection = SmackConnectionFactory(setupSmackConnection),
            features = cryptonFeatures(),
            navigateChatId = R.id.chatFragment
        )
    }

    override fun onCreate() {
        super.onCreate()
        initExceptionService()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        scope.initAndroidLog()
        initAppDebug()
        registerActivityLifecycleCallbacks(ActivityLifecycleLogger)
        initSmack(cacheDir.resolve(OMEMO_STORE_NAME))
        scope.apply {
            service().dispatch(Subscribe.AppService)
            launch { newSessionsFlow().collect { currentAccount = it.address } }
        }
    }

    private companion object {
        private const val OMEMO_STORE_NAME = "omemo"
    }
}
