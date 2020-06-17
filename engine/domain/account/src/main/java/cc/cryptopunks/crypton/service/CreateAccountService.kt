package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.context.Connectable
import cc.cryptopunks.crypton.context.Connector
import cc.cryptopunks.crypton.context.createHandlers
import cc.cryptopunks.crypton.context.dispatch
import cc.cryptopunks.crypton.context.plus
import cc.cryptopunks.crypton.handler.handleLogin
import cc.cryptopunks.crypton.handler.handleRegister
import cc.cryptopunks.crypton.handler.handleSetField
import cc.cryptopunks.crypton.util.Store
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CreateAccountService(
    private val appScope: AppScope
) : Connectable {

    private val log = typedLog()
    private val form = Form()

    override val coroutineContext = SupervisorJob() + Dispatchers.Default

    private val handlers = createHandlers {
        with(appScope) {
            plus(handleSetField(form))
            plus(handleRegister(form))
            plus(handleLogin(form))
        }
    }

    override fun Connector.connect(): Job = launch {
        log.d("Start")
        input.collect {
            log.d(it)
            handlers.dispatch(it, output)?.join()
        }
    }
}

class Form(fields: Map<Account.Field, CharSequence> = emptyMap()) :
    Store<Map<Account.Field, CharSequence>>(fields)
