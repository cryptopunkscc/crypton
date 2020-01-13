package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Clip
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Service
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MessageService(
    val message: Message,
    private val address: Address,
    private val copyToClipboard: Clip.Board.Sys.SetClip
) : Service {

    override val coroutineContext =
        SupervisorJob() + Dispatchers.Main

    override val id get() = message.id

    val isAccountMessage get() = message.from.address == address

    object Copy

    class State(
        val message: String,
        val date: Long,
        val author: String,
        val status: String
    )

    private val state = State(
        message = message.text,
        date = message.timestamp,
        author = message.from.address.local,
        status = message.status.name
    )

    override fun Service.Binding.bind(): Job = launch {
        state.out()
        input.collect { arg ->
            when (arg) {
                is Copy -> copyToClipboard(message.text)
            }
        }
    }

    class Factory @Inject constructor(
        address: Address,
        copyToClipboard: Clip.Board.Sys.SetClip
    ) : (Message) -> MessageService by { message ->
        MessageService(
            address = address,
            message = message,
            copyToClipboard = copyToClipboard
        )
    }
}