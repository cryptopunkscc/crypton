package cc.cryptopunks.crypton.module

import android.app.Application
import android.os.Handler
import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.smack.SmackClientFactory
import dagger.Module
import dagger.Provides
import org.jivesoftware.smack.ConnectionConfiguration
import java.net.InetAddress
import javax.inject.Singleton


@Module
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
    fun clientFactory(): Client.Factory = SmackClientFactory {
        setResource("xmpptest")
        setHostAddress(InetAddress.getByName("10.0.2.2"))
        setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
    }
}