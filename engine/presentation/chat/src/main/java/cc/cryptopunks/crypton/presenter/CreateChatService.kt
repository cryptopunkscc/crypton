package cc.cryptopunks.crypton.presenter

import cc.cryptopunks.crypton.context.*
import cc.cryptopunks.crypton.interactor.CreateChatInteractor
import cc.cryptopunks.crypton.interactor.CreateChatInteractor.*
import javax.inject.Inject

class CreateChatService @Inject constructor(
    override val scope: Feature.Scope,
    private val createChat: CreateChatInteractor,
    private val navigate: Route.Api.Navigate
) : Service.Abstract() {

    override suspend fun Any.onInput() {
        when (this) {
            is Input.CreateChat -> try {
                val data = Data(
                    title = userAddress.id,
                    users = listOf(User(userAddress))
                )
                createChat(data).run {
                    val address = await().address
                    Route.Chat()
                        .apply { chatAddress = address.id }
                        .let(navigate)
                }
            } catch (throwable: Throwable) {
                throwable.out()
            }
        }
    }

    interface Input {
        data class CreateChat(val userAddress: Address) : Input
    }

    interface Core {
        val createChatService: CreateChatService
    }
}