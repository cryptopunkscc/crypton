package cc.cryptopunks.crypton.util.logger

import cc.cryptopunks.crypton.util.Log
import java.util.*

object DumbLog : Log.Output {

    override fun invoke(event: Log.Event) {
        event.run {
            println("-")
            if (scopes.isNotEmpty()) println(scopes.joinToString("/", prefix = "/"))
            if (message.isNotBlank()) println(message)
            if (action != Unit ) println(action)
            println(Date(timestamp))
            throwable?.printStackTrace()
            println()
        }
    }
}
