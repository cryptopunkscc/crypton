package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.asDep
import cc.cryptopunks.crypton.cryptonContext

fun Sys.context() = cryptonContext(
    indicatorSys.asDep(),
    notificationSys.asDep(),
    clipboardSys.asDep(),
    networkSys.asDep(),
    deviceSys.asDep(),
    executeSys.asDep(),
    uriSys.asDep(),
    cryptoSys.asDep(),
    fileSys.asDep(),
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
