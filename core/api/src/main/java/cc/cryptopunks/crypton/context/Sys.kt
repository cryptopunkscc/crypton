package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.create.cryptonContext
import cc.cryptopunks.crypton.create.dep

fun Sys.context() = cryptonContext(
    indicatorSys.dep(),
    notificationSys.dep(),
    clipboardSys.dep(),
    networkSys.dep(),
    deviceSys.dep(),
    executeSys.dep(),
    uriSys.dep(),
    cryptoSys.dep(),
    fileSys.dep(),
)

interface Sys {

    val indicatorSys: Indicator.Sys
    val notificationSys: Notification.Sys
    val clipboardSys: Clip.Board.Sys
    val networkSys: Network.Sys
    val deviceSys: Device.Sys
    val executeSys: Execute.Sys
    val uriSys: URI.Sys
    val cryptoSys: Crypto.Sys
    val fileSys: File.Sys
}
