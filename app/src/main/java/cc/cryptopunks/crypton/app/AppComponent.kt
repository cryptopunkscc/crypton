package cc.cryptopunks.crypton.app

import cc.cryptopunks.crypton.app.module.ApplicationModule
import cc.cryptopunks.crypton.app.module.CoreModule
import cc.cryptopunks.crypton.app.util.ActivityStack
import cc.cryptopunks.crypton.app.util.AsyncExecutor
import cc.cryptopunks.crypton.common.HandleError
import cc.cryptopunks.crypton.core.entity.Account
import cc.cryptopunks.crypton.core.repository.AccountRepository
import cc.cryptopunks.crypton.xmpp.Xmpp
import cc.cryptopunks.kache.core.Kache
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApplicationModule::class,
        CoreModule::class
    ]
)
interface AppComponent {

    val provideKache: Kache.Provider
    val activityStackCache: ActivityStack.Cache
    val executeAsync: AsyncExecutor
    val accountRepository: AccountRepository
    val accountDao: Account.Dao
    val createXmpp: Xmpp.Factory
    val xmppCache: Xmpp.Cache
    val handleError: HandleError
    val errorPublisher: HandleError.Publisher
}