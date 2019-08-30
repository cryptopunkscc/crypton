package cc.cryptopunks.crypton.account

import android.app.Application
import androidx.room.Room
import cc.cryptopunks.crypton.account.domain.command.*
import cc.cryptopunks.crypton.common.HandleError
import cc.cryptopunks.crypton.common.SingletonQualifier
import cc.cryptopunks.crypton.core.data.CoreDatabase
import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.account.domain.repository.AccountRepository
import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.smack.SmackClientFactory
import dagger.Component
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import org.jivesoftware.smack.ConnectionConfiguration
import java.net.InetAddress
import javax.inject.Singleton

@Singleton
@Component(modules = [TestModule::class])
internal interface IntegrationTestComponent {

    @get:SingletonQualifier
    val disposable: CompositeDisposable
    val database: CoreDatabase
    val accountDao: Account.Dao
    val clientCache: Client.Cache
    val accountRepository: AccountRepository

    val addAccount: AddAccount
    val connectAccount: ConnectAccount
    val createAccount: CreateAccount
    val disconnectAccount: DisconnectAccount
    val removeAccount: RemoveAccount
    val deleteAccount: DeleteAccount

    val reconnectAccounts: ReconnectAccounts
}

@Module
internal class TestModule(
    @get:Provides
    val context: Application
) {

    init {
        smackFactory
    }

    @get:Provides
    @get:SingletonQualifier
    val disposable = CompositeDisposable()

    @Provides
    @Singleton
    fun appDatabase(context: Application) = Room
        .inMemoryDatabaseBuilder(context, CoreDatabase::class.java)
        .build()

    @Provides
    @Singleton
    fun accountDao(coreDatabase: CoreDatabase) = coreDatabase
        .accountDao

    @Provides
    @Singleton
    fun handleError(): HandleError = object : HandleError {
        override fun invoke(throwable: Throwable) = throw throwable
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

