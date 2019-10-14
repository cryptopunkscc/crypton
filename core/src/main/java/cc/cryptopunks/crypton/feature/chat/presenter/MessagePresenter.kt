package cc.cryptopunks.crypton.feature.chat.presenter

import cc.cryptopunks.crypton.actor.Actor
import cc.cryptopunks.crypton.entity.Message
import cc.cryptopunks.crypton.presenter.Presenter
import javax.inject.Inject

data class MessagePresenter(
    private val message: Message
) : Presenter<MessagePresenter.View> {

    val id get() = message.id

    override suspend fun View.invoke() = run {
        setMessage(message.text)
        setDate(message.timestamp)
    }

    interface View : Actor {
        fun setMessage(text: String)
        fun setDate(timestamp: Long)
        fun setAuthor(name: String)
    }

    class Factory @Inject constructor() : (Message) -> MessagePresenter by { message ->
        MessagePresenter(message)
    }
}