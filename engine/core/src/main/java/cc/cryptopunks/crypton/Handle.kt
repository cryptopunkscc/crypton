package cc.cryptopunks.crypton

typealias Handle<A> = suspend RequestScope.(out: Output, action: A) -> Unit
typealias TypedOutput<T> = suspend T.() -> Unit
typealias Output = TypedOutput<Any>
