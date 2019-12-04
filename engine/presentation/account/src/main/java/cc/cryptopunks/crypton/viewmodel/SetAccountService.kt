package cc.cryptopunks.crypton.viewmodel

import cc.cryptopunks.crypton.context.Feature
import cc.cryptopunks.crypton.context.Route
import cc.cryptopunks.crypton.context.Service
import cc.cryptopunks.crypton.viewmodel.SetAccountService.Input.AddAccount
import cc.cryptopunks.crypton.viewmodel.SetAccountService.Input.RegisterAccount
import javax.inject.Inject

class SetAccountService @Inject constructor(
    override val scope: Feature.Scope,
    private val navigate: Route.Api.Navigate
) : Service.Abstract() {

    override suspend fun Any.onInput() {
        when (this) {
            is AddAccount -> navigate(Route.Login)
            is RegisterAccount -> navigate(Route.Register)
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