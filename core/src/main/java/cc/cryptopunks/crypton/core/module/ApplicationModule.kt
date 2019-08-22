package cc.cryptopunks.crypton.core.module

import android.app.Application
import android.os.Handler
import cc.cryptopunks.crypton.core.util.KacheProviderFactory
import cc.cryptopunks.crypton.core.util.MainErrorHandler
import cc.cryptopunks.crypton.common.HandleError
import cc.cryptopunks.crypton.common.SingletonQualifier
import cc.cryptopunks.crypton.smack.SmackClientFactory
import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.kache.core.Kache
import cc.cryptopunks.kache.core.KacheManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import org.jivesoftware.smack.ConnectionConfiguration
import java.net.InetAddress
import javax.inject.Singleton


@Module(includes = [ApplicationModule.Bindings::class])
class ApplicationModule(
    @get:Provides
    @get:Singleton
    val application: Application
) {
    @get:Provides
    @get:Singleton
    val handler = Handler()

    @Provides
    @Singleton
    fun kacheManager(provide: KacheProviderFactory): KacheManager = provide() as KacheManager

    @Provides
    @Singleton
    fun kacheProvider(kacheManager: KacheManager): Kache.Provider = kacheManager

    @Provides
    @Singleton
    @SingletonQualifier
    fun disposable() = CompositeDisposable()

    @Provides
    @Singleton
    fun clientFactory(): Client.Factory = SmackClientFactory {
        setResource("xmpptest")
        setHostAddress(InetAddress.getByName("10.0.2.2"))
        setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
    }

    @Module
    interface Bindings {

        @Binds
        @Singleton
        fun handleError(handler: MainErrorHandler): HandleError

        @Binds
        @Singleton
        fun errorPublisher(handler: MainErrorHandler): HandleError.Publisher
    }
}