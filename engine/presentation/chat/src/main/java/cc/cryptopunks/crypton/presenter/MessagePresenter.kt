package cc.cryptopunks.crypton.presenter

import cc.cryptopunks.crypton.actor.Actor
import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Clipboard
import cc.cryptopunks.crypton.context.Message
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MessagePresenter(
    private val address: Address,
    private val message: Message,
    private val copyToClipboard: Clipboard.Sys.SetClip
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
        coroutineScope {
            launch { optionSelections.collect(onOptionSelected) }
        }
    }

    interface View : Actor {
        fun setMessage(text: String)
        fun setDate(timestamp: Long)
        fun setAuthor(name: String)
        val optionSelections: Flow<Option>
    }

    enum class Option { Copy }

    class Factory @Inject constructor(
        address: Address,
        copyToClipboard: Clipboard.Sys.SetClip
    ) : (Message) -> MessagePresenter by { message ->
        MessagePresenter(
            address = address,
            message = message,
            copyToClipboard = copyToClipboard
        )
    }
}