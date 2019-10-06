package cc.cryptopunks.crypton.feature.chat.presenter

import cc.cryptopunks.crypton.entity.Message
import cc.cryptopunks.crypton.util.Presenter
import java.util.*
import javax.inject.Inject

data class MessagePresenter(
    private val message: Message
) : Presenter<MessagePresenter.View> {

    val text get() = message.text
    val date get() = Date(message.timestamp)

    override suspend fun View.invoke() = run {
        setText(message.text)
        setDate(message.timestamp)
    }

    interface View {
        fun setText(text: String)
        fun setDate(timestamp: Long)
    }

    class Factory @Inject constructor() : (Message) -> MessagePresenter by ::MessagePresenter
}