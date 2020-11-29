package cc.cryptopunks.crypton.context

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
