package cc.cryptopunks.crypton.log

import cc.cryptopunks.crypton.RequestLog

val printOnFinish: suspend List<RequestLog.Data>.() -> Unit = {
    when {
        isEmpty() -> Unit
        else -> when (last().data) {
            is Throwable -> printAll()
            else -> printLast()
        }
    }
}

val printAll: suspend List<RequestLog.Data>.() -> Unit = {
    println()
    forEach { println(it.formatMessage()) }
    println()
}

val printLast: suspend List<RequestLog.Data>.() -> Unit = {
    println(last().formatMessage())
}
