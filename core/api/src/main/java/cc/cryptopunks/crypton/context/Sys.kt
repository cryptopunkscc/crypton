package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.asDep
import cc.cryptopunks.crypton.cryptonContext

fun Sys.context() = cryptonContext(
    indicatorSys.asDep<Indicator.Sys>(),
    notificationSys.asDep<Notification.Sys>(),
    clipboardSys.asDep<Clip.Board.Sys>(),
    networkSys.asDep<Network.Sys>(),
    deviceSys.asDep<Device.Sys>(),
    executeSys.asDep<Execute.Sys>(),
    uriSys.asDep<URI.Sys>(),
    cryptoSys.asDep<Crypto.Sys>(),
    fileSys.asDep<File.Sys>(),
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
