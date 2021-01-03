package cc.cryptopunks.crypton

typealias Handle<A> = suspend RequestScope.(out: Output, action: A) -> Unit
typealias Output = suspend Any.() -> Unit
