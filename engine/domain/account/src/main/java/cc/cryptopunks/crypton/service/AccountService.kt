package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.context.Connectable
import cc.cryptopunks.crypton.context.Connector
import cc.cryptopunks.crypton.context.Route
import cc.cryptopunks.crypton.context.createHandlers
import cc.cryptopunks.crypton.context.dispatch
import cc.cryptopunks.crypton.handler.handleAccountListSubscription
import cc.cryptopunks.crypton.handler.handleLogin
import cc.cryptopunks.crypton.handler.handleLogout
import cc.cryptopunks.crypton.handler.handleRegister
import cc.cryptopunks.crypton.handler.handleRemove
import cc.cryptopunks.crypton.handler.handleSetField
import cc.cryptopunks.crypton.selector.newAccountConnectedFlow
import cc.cryptopunks.crypton.util.Store
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AccountService(
    private val appScope: AppScope
) : AppScope by appScope,
    Connectable {

    override val coroutineContext = SupervisorJob() + Dispatchers.Default

    private val form = Form()

    private val handlers = createHandlers {
        +handleSetField(form)
        +handleRegister(form)
        +handleLogin(form)
        +handleLogout()
        +handleRemove()
        +handleAccountListSubscription()
    }

    override fun Connector.connect(): Job = launch {
        log.d("Start $id")
        input.collect {
            log.d("$id $it")
            handlers.dispatch(it, output)?.join()
        }
    }
}

internal class Form(fields: Map<Account.Field, CharSequence> = emptyMap()) :
    Store<Map<Account.Field, CharSequence>>(fields)


suspend fun AppScope.handleAccountConnections() = newAccountConnectedFlow().collect {
    navigate(Route.AccountList)
}
