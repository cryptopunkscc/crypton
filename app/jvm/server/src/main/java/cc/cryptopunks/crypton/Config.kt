package cc.cryptopunks.crypton


fun Server.Config.default() = apply {
    home = "~/.crypton".replaceFirst("~", System.getProperty("user.home"))
    omemoStore = "omemo_store"
    socketPort = 2323
    socketAddress = "127.0.0.1"
    hostAddress = null
    securityMode = "ifPossible"
    inMemory = "false"
}

fun Server.Config.local() = apply {
    hostAddress = "127.0.0.1"
    securityMode = "disabled"
}

val Server.Config.omemoStorePath get() = "$home/$omemoStore"
