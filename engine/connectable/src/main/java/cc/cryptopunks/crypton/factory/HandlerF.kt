package cc.cryptopunks.crypton.factory

import cc.cryptopunks.crypton.Action
import cc.cryptopunks.crypton.Handle
import kotlinx.coroutines.CoroutineScope

fun <S: CoroutineScope, A: Action> handler(handle: Handle<S, A>) = handle
