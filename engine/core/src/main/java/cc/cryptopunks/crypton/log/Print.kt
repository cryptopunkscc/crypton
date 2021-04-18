package cc.cryptopunks.crypton.log

import cc.cryptopunks.crypton.RequestLog

typealias PrintRequestLogData = suspend List<RequestLog.Data>.() -> Unit

val printOnFinish: PrintRequestLogData = {
    when {
        isEmpty() -> Unit
        else -> when (last().data) {
            is Throwable -> printGroup()
            else -> printLast()
        }
    }
}

val printGroup: PrintRequestLogData = {
    println()
    forEach { println(it.formatMessage()) }
    println()
}

val printLast: PrintRequestLogData = {
    println(last().formatMessage())
}
