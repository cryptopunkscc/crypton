package cc.cryptopunks.crypton.viewmodel

import cc.cryptopunks.crypton.context.Route
import cc.cryptopunks.crypton.context.Service
import cc.cryptopunks.crypton.viewmodel.SetAccountService.Input.AddAccount
import cc.cryptopunks.crypton.viewmodel.SetAccountService.Input.RegisterAccount
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class SetAccountService @Inject constructor(
    private val navigate: Route.Api.Navigate
) : Service {

    override val coroutineContext = SupervisorJob() + Dispatchers.IO

    override fun Service.Binding.bind(): Job = launch {
        input.collect { arg ->
            when (arg) {
                is AddAccount -> navigate(Route.Login)
                is RegisterAccount -> navigate(Route.Register)
            }
        }
    }

    interface Input {
        object AddAccount : Input
        object RegisterAccount : Input
    }

    interface Core {
        val setAccountService: SetAccountService
    }
}