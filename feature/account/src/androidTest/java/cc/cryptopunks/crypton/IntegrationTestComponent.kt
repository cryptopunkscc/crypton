package cc.cryptopunks.crypton

import android.app.Application
import androidx.room.Room
import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.data.Database
import cc.cryptopunks.crypton.domain.interactor.*
import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.smack.SmackClientFactory
import cc.cryptopunks.crypton.util.BroadcastError
import dagger.Component
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import org.jivesoftware.smack.ConnectionConfiguration
import java.net.InetAddress
import javax.inject.Singleton

@Singleton
@Component(modules = [TestModule::class])
internal interface IntegrationTestComponent {

    val database: Database
    val accountRepo: Account.Repo
    val clientCache: Client.Cache

    val addAccount: AddAccountInteractor
    val connectAccount: ConnectAccountInteractor
    val registerAccount: RegisterAccountInteractor
    val disconnectAccount: DisconnectAccountInteractor
    val deleteAccount: DeleteAccountInteractor
    val unregisterAccount: UnregisterAccountInteractor

    val reconnectAccounts: ReconnectAccountsInteractor
}

@Module
internal class TestModule(
    @get:Provides
    val context: Application
) {

    init {
        smackFactory
    }

    @Provides
    @Singleton
    fun appDatabase(context: Application) = Room
        .inMemoryDatabaseBuilder(context, Database::class.java)
        .build()

    @Provides
    @Singleton
    fun accountDao(database: Database) = database
        .accountRepo

    @Provides
    @Singleton
    @InternalCoroutinesApi
    fun handleError(): BroadcastError = object : BroadcastError, Flow<Throwable> {
        override fun invoke(throwable: Throwable) = throw throwable
        override suspend fun collect(collector: FlowCollector<Throwable>) {}
    }

    @Provides
    @Singleton
    fun clientFactory(): Client.Factory = smackFactory
}

internal val smackFactory by lazy {
    SmackClientFactory {
        setResource("xmpptest")
        setHostAddress(InetAddress.getByName("10.0.2.2"))
        setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
    }
}

