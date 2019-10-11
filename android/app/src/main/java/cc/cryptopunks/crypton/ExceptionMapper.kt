package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.smack.util.MapClientException

object ExceptionMapper : (Throwable) -> (Throwable) by { throwable ->
    listOf(
        MapClientException
    ).map(throwable)
}

private fun <T: Any> List<(T) -> T?>.map(arg: T) = this
    .mapNotNull { it(arg) }
    .firstOrNull()
    ?: arg