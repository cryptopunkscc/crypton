package cc.cryptopunks.crypton

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import cc.cryptopunks.crypton.activity.MainActivity
import cc.cryptopunks.crypton.context.ApplicationId
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Connection
import cc.cryptopunks.crypton.context.Core
import cc.cryptopunks.crypton.context.Main
import cc.cryptopunks.crypton.context.Notification
import cc.cryptopunks.crypton.context.Subscribe
import cc.cryptopunks.crypton.context.account
import cc.cryptopunks.crypton.context.context
import cc.cryptopunks.crypton.context.createRootScope
import cc.cryptopunks.crypton.debug.drawer.initAppDebug
import cc.cryptopunks.crypton.feature.androidFeatures
import cc.cryptopunks.crypton.feature.androidResolvers
import cc.cryptopunks.crypton.fragment.AndroidChatNotificationFactory
import cc.cryptopunks.crypton.room.RoomAppRepo
import cc.cryptopunks.crypton.navigate.currentAccount
import cc.cryptopunks.crypton.selector.newSessionsFlow
import cc.cryptopunks.crypton.service.cryptonFeatures
import cc.cryptopunks.crypton.service.initExceptionService
import cc.cryptopunks.crypton.service.cryptonResolvers
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

    private val mainActivityClass = Main(MainActivity::class.java)
    private val features = cryptonFeatures() + androidFeatures()

    override val scope by lazy {
        createRootScope(
            cryptonContext(
                ApplicationId(BuildConfig.APPLICATION_ID),
                mainActivityClass,
                MainExecutor(Dispatchers.Main.asExecutor()),
                IOExecutor(Dispatchers.IO.asExecutor()),
                RoomAppRepo(this).context(),
                AndroidSys(
                    application = this,
                    notificationFactories = mapOf(
                        Notification.Messages::class to AndroidChatNotificationFactory(
                            context = this,
                            mainActivityClass = mainActivityClass,
                            navGraphId = R.navigation.main
                        )
                    ),
                    appNameResId = R.string.app_name,
                    smallIconResId = R.mipmap.ic_launcher_round
                ).context(),
                SmackConnectionFactory(setupSmackConnection).asDep<Connection.Factory>(),
                features,
                features.createHandlers(),
                cryptonResolvers() + androidResolvers(),
                Chat.NavigationId(R.id.chatFragment)
            )
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
            launch { newSessionsFlow().collect { currentAccount = it.account.address } }
        }
    }

    private companion object {
        private const val OMEMO_STORE_NAME = "omemo"
    }
}
