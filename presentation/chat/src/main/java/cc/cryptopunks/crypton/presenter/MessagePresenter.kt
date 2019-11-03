package cc.cryptopunks.crypton.presenter

import cc.cryptopunks.crypton.actor.Actor
import cc.cryptopunks.crypton.entity.Address
import cc.cryptopunks.crypton.entity.Message
import javax.inject.Inject

data class MessagePresenter(
    private val address: Address,
    private val message: Message
) : Presenter<MessagePresenter.View> {

    val id get() = message.id

    val isAccountMessage get() = message.from.address == address

    override suspend fun View.invoke(): Unit = message.run {
        setMessage(text)
        setDate(timestamp)
        setAuthor(from.address.local)
    }

    interface View : Actor {
        fun setMessage(text: String)
        fun setDate(timestamp: Long)
        fun setAuthor(name: String)
    }

    class Factory @Inject constructor(
        address: Address
    ) : (Message) -> MessagePresenter by { message ->
        MessagePresenter(
            address = address,
            message = message
        )
    }
}