package cc.cryptopunks.crypton.smack.net.exception

import cc.cryptopunks.crypton.context.Net
import cc.cryptopunks.crypton.util.Broadcast

internal class NetErrors :
    Broadcast<Net.Exception>(),
    Net.Exception.Output {

    suspend fun send(throwable: Throwable?) =
        send(Net.Exception(cause = throwable))
}