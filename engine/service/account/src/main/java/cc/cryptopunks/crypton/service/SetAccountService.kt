package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.Route
import cc.cryptopunks.crypton.context.Service
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class SetAccountService @Inject constructor(
    private val navigate: Route.Api.Navigate
) : Service {

    object AddAccount
    object RegisterAccount

    override val coroutineContext = SupervisorJob() + Dispatchers.IO

    override fun Service.Connector.connect(): Job = launch {
        input.collect { arg ->
            when (arg) {
                is AddAccount -> navigate(Route.Login)
                is RegisterAccount -> navigate(Route.Register)
            }
        }
    }

    interface Core {
        val setAccountService: SetAccountService
    }
}