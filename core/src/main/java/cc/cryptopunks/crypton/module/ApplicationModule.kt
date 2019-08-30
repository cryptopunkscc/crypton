package cc.cryptopunks.crypton.module

import android.app.Application
import android.os.Handler
import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.util.HandleError
import cc.cryptopunks.crypton.util.SingletonQualifier
import cc.cryptopunks.crypton.util.MainErrorHandler
import cc.cryptopunks.crypton.smack.SmackClientFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
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
    @SingletonQualifier
    fun disposable() = CompositeDisposable()

    @Provides
    @Singleton
    fun schedulers() = cc.cryptopunks.crypton.util.Schedulers(
        main = AndroidSchedulers.mainThread(),
        io = Schedulers.io()
    )

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
        fun handleError(handler: MainErrorHandler): HandleError

        @Binds
        fun errorPublisher(handler: MainErrorHandler): HandleError.Publisher
    }
}