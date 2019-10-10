package cc.cryptopunks.crypton.util.ext

import kotlinx.coroutines.CompletionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.Job

fun CoroutineScope.invokeOnClose(handler: CompletionHandler): DisposableHandle =
    coroutineContext[Job]!!.invokeOnCompletion(handler)
