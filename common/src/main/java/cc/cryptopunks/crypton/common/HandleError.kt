package cc.cryptopunks.crypton.common

interface HandleError : (Throwable) -> Unit {

    interface Publisher : RxPublisher<Throwable>
}

fun handleError(handle: (Throwable) -> Unit): HandleError =
    object : HandleError, (Throwable) -> Unit by handle {}