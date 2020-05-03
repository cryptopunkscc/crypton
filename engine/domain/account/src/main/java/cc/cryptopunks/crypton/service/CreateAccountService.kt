package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.Connectable
import cc.cryptopunks.crypton.context.Connector
import cc.cryptopunks.crypton.interactor.AddAccountInteractor
import cc.cryptopunks.crypton.util.Form
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class CreateAccountService internal constructor(
    private val addAccount: AddAccountInteractor,
    private val initial: Form = Form()
) : Connectable,
    AccountForm {

    interface Action
    object Register : Action
    object Login : Action

    override val coroutineContext = SupervisorJob() + Dispatchers.IO

    private val formService = FormService(coroutineContext)

    override fun Connector.connect(): Job = launch {
        connect(formService)
        launch {
            if (initial.fields.isNotEmpty()) initial.out()
            input.onEach {
                println(it)
            }.filterIsInstance<Action>().collect { action ->
                addAccount(
                    account = formService.form.account(),
                    register = action is Register
                )
            }
        }
    }
}
