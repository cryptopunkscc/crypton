package cc.cryptopunks.crypton.presenter

import cc.cryptopunks.crypton.context.Actor
import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Clip
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Presenter
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MessagePresenter(
    val message: Message,
    private val address: Address,
    private val copyToClipboard: Clip.Board.Sys.SetClip
) : Presenter<MessagePresenter.View> {

    val id get() = message.id

    val isAccountMessage get() = message.from.address == address

    private val onOptionSelected: suspend (Option) -> Unit = { option ->
        when (option) {
            Option.Copy -> copyToClipboard(message.text)
        }
    }

    override suspend fun View.invoke(): Unit = message.run {
        setMessage(text)
        setDate(timestamp)
        setAuthor(from.address.local)
        setStatus(status)
        coroutineScope {
            launch { optionSelections.collect(onOptionSelected) }
        }
    }

    interface View : Actor {
        fun setMessage(text: String)
        fun setDate(timestamp: Long)
        fun setAuthor(name: String)
        fun setStatus(status: Message.Status)
        val optionSelections: Flow<Option>
    }

    enum class Option { Copy }

    class Factory @Inject constructor(
        address: Address,
        copyToClipboard: Clip.Board.Sys.SetClip
    ) : (Message) -> MessagePresenter by { message ->
        MessagePresenter(
            address = address,
            message = message,
            copyToClipboard = copyToClipboard
        )
    }
}