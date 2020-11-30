package cc.cryptopunks.crypton.util

operator fun <R, T> R?.invoke(ifNotNull: R.() -> T): T? = this?.ifNotNull()
