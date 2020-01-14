package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.util.Form
import cc.cryptopunks.crypton.context.Service
import cc.cryptopunks.crypton.util.TextField
import cc.cryptopunks.crypton.interactor.AddAccountInteractor
import cc.cryptopunks.crypton.interactor.RegisterAccountInteractor
import cc.cryptopunks.crypton.service.AccountForm.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch
import javax.inject.Inject

open class SignService @Inject constructor(
    private val request: (Account) -> Job,
    private val initial: Form = Form()
) : Service,
    AccountForm {


    override val coroutineContext = SupervisorJob() + Dispatchers.IO

    private val formService = FormService(coroutineContext)

    override fun Service.Connector.connect(): Job = launch {
        connect(formService)
        launch {
            if (initial.fields.isNotEmpty()) initial.out()
            input.filterIsInstance<OnClick>().collect {
                formService.form.account().let(request)
            }
        }
    }
}

class SignInService @Inject constructor(
    addAccount: AddAccountInteractor
) : SignService(
    request = addAccount,
    initial = Form(
        TextField(ServiceName, "cryptopunks.cc"),
        TextField(UserName, "test"),
        TextField(Password, "test")
    )
) {
    interface Core {
        val signInService: SignInService
    }
}


class SignUpService @Inject constructor(
    registerAccount: RegisterAccountInteractor
) : SignService(registerAccount) {

    interface Core {
        val signUpService: SignUpService
    }
}