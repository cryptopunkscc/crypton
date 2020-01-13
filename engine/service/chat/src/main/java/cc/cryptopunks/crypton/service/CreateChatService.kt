package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Route
import cc.cryptopunks.crypton.context.Service
import cc.cryptopunks.crypton.context.User
import cc.cryptopunks.crypton.interactor.CreateChatInteractor
import cc.cryptopunks.crypton.interactor.CreateChatInteractor.Data
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class CreateChatService @Inject constructor(
    private val createChat: CreateChatInteractor,
    private val navigate: Route.Api.Navigate
) : Service {

    data class CreateChat(val userAddress: Address)

    override val coroutineContext = SupervisorJob() + Dispatchers.IO

    override fun Service.Binding.bind(): Job = launch {
        input.collect { arg ->
            when (arg) {
                is CreateChat -> try {
                    val data = Data(
                        title = arg.userAddress.id,
                        users = listOf(User(arg.userAddress))
                    )
                    createChat(data).run {
                        val address = await().address
                        Route.Chat()
                            .apply { chatAddress = address.id }
                            .let(navigate)
                    }
                } catch (throwable: Throwable) {
                    output(throwable)
                }
            }
        }
    }

    interface Core {
        val createChatService: CreateChatService
    }
}