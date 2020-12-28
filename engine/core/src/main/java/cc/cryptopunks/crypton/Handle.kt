package cc.cryptopunks.crypton

import kotlinx.coroutines.CoroutineScope

typealias Handle<A> = suspend CoroutineScope.(out: Output, action: A) -> Unit
typealias Output = suspend Any.() -> Unit
