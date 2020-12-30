package cc.cryptopunks.crypton.util.logger

import cc.cryptopunks.crypton.util.Log

object VoidLog : Log.Output {
    override fun invoke(any: Any) = Unit
}
