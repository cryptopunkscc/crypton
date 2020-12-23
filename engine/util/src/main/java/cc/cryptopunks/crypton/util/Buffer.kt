package cc.cryptopunks.crypton.util

data class Buffer<T>(val bufferSize: Int = 32, val list: List<T> = emptyList()) : List<T> by list {
    operator fun plus(element: T) = copy(list = list.plus(element).run { if (size > bufferSize) drop(1) else this })
}
