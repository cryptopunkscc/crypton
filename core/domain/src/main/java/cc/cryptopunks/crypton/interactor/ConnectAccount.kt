package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.Output
import cc.cryptopunks.crypton.context.Account

internal suspend fun Account.connectAccount(
    out: Output,
    connect: suspend (Account) -> Unit
) {
    Account.Connecting(address).out()
    try {
        connect(this)
        Account.Connected(address)
    } catch (e: Throwable) {
        e.printStackTrace()
        Account.Error(address, e.message)
    }.out()
}
