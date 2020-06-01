package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.*
import cc.cryptopunks.crypton.interactor.AddAccountInteractor
import cc.cryptopunks.crypton.util.Store
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CreateAccountService internal constructor(
    private val addAccount: AddAccountInteractor
) : Connectable {

    override val coroutineContext = SupervisorJob() + Dispatchers.IO

    private val form = Form()

    private val log = typedLog()

    override fun Connector.connect(): Job = launch {
        log.d("Start")
        input.collect { arg ->
            log.d(arg)
            when (arg) {
                is Account.Service.Login,
                is Account.Service.Register -> form.account().let { account ->
                    Account.Service.Connecting(account.address).out()
                    addAccount(
                        account = account,
                        register = arg is Account.Service.Register
                    )
                    Account.Service.Connected(account.address).out()
                }
                is Account.Service.Set -> form reduce {
                    plus(arg.field to arg.text)
                }
            }
        }
    }
}

class Form(fields: Map<Account.Field, CharSequence> = emptyMap()) :
    Store<Map<Account.Field, CharSequence>>(fields)

private fun Form.account() = Account(
    address = Address(
        local = get()[Account.Field.UserName].toString(),
        domain = get()[Account.Field.ServiceName].toString()
    ),
    password = Password(get()[Account.Field.Password]!!)
)
