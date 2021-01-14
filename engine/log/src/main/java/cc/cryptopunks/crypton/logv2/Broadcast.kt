package cc.cryptopunks.crypton.logv2

interface LogBroadcast {
    fun connect(vararg outputs: LogOutput): Any
}
