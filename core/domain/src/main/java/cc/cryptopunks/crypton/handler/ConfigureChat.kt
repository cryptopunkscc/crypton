import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.handle

internal fun handleConfigureChat() = handle { _, _: Exec.ConfigureConference ->
    configureConference(chat.address)
}
