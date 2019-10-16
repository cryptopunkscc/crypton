package cc.cryptopunks.crypton.feature.chat.presenter

import cc.cryptopunks.crypton.actor.Actor
import cc.cryptopunks.crypton.entity.Address
import cc.cryptopunks.crypton.entity.Message
import cc.cryptopunks.crypton.presenter.Presenter
import javax.inject.Inject

data class MessagePresenter(
    private val address: Address,
    private val message: Message
) : Presenter<MessagePresenter.View> {

    val id get() = message.id

    override suspend fun View.invoke() = run {
        setMessage(message.text)
        setDate(message.timestamp)
        setAuthor(message.from.id)
        alignRight(message.from.address == address)
    }

    interface View : Actor {
        fun setMessage(text: String)
        fun setDate(timestamp: Long)
        fun setAuthor(name: String)
        fun alignRight(value: Boolean)
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