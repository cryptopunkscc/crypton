package cc.cryptopunks.crypton.util.logger

import cc.cryptopunks.crypton.util.Log
import java.util.*

object DumbLog : Log.Output {

    override fun invoke(event: Any) {
        (event as? Log.Event)?.run {
            println("-")
            if (scopes.isNotEmpty()) println(scopes.joinToString("/", prefix = "/"))
            if (message.isNotBlank()) println(message)
            println(Date(timestamp))
            throwable?.printStackTrace()
            println()
        }
    }
}
