package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.*
import cc.cryptopunks.crypton.interactor.CreateChatInteractor
import cc.cryptopunks.crypton.interactor.CreateChatInteractor.Data
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CreateChatService internal constructor(
    private val createChat: CreateChatInteractor,
    private val navigate: Route.Navigate
) : Connectable {

    override val coroutineContext = SupervisorJob() + Dispatchers.IO

    override fun Connector.connect(): Job = launch {
        input.collect { arg ->
            when (arg) {
                is Chat.Service.CreateChat -> try {
                    val data = Data(
                        title = arg.address.id,
                        users = listOf(User(arg.address))
                    )
                    createChat(data).run {
                        Route.Chat().also {
                            it.chatAddress = address.id
                        }.let(navigate)
                    }
                } catch (throwable: Throwable) {
                    output(throwable)
                }
            }
        }
    }
}
